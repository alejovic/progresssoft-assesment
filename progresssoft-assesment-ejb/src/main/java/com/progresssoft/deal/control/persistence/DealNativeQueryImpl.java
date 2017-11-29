package com.progresssoft.deal.control.persistence;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TemporalType;

import com.progresssoft.deal.control.validator.FileDealValidatorException;
import com.progresssoft.deal.control.validator.IFileDealValidator;
import com.progresssoft.deal.entity.dto.DataFileOutDTO;
import com.progresssoft.deal.entity.dto.DealDTO;
import com.progresssoft.deal.entity.model.PsDealData;
import com.progresssoft.deal.entity.model.PsFile;

public class DealNativeQueryImpl extends BaseDealPersistence {

	private static final long serialVersionUID = 1L;

	public DealNativeQueryImpl() {
	}

	public DataFileOutDTO execute() {

		List<String> lines = null;
		DataFileOutDTO dataFileOutDTO = null;
		EntityManager em = null;
		EntityTransaction transaction = null;

		try {
			lines = getLines();
			dataFileOutDTO = new DataFileOutDTO();

			int entityCount = lines.size();
			int batchSize = 1000;

			em = createEntityManager();
			transaction = em.getTransaction();

			transaction.begin();

			StringBuffer sql = new StringBuffer();
			sql.append("INSERT INTO ");
			sql.append("	ps_deal_data (ID, FK_ID_FILE, DEAL_ID, ISO_SOURCE, ISO_TARGET, DATE, AMOUNT) ");
			sql.append("	VALUES ");
			sql.append("	 (?1, ?2, ?3, ?4, ?5, ?6, ?7) ");

			for (int index = 0; index < entityCount; index++) {

				dataFileOutDTO.addCountLine();

				if (index > 0 && index % batchSize == 0) {
					em.flush();
					em.clear();

					transaction.commit();
					transaction.begin();
				}

				try {
					String line = lines.get(index);
					String[] fields = line.split(DEFAULT_SEPARATOR);
					final DealDTO dealDTO = new DealDTO();
					dealDTO.setId(fields[0]);
					dealDTO.setIsoSource(fields[1]);
					dealDTO.setIsoTarget(fields[2]);
					dealDTO.setDate(fields[3]);
					dealDTO.setAmount(fields[4]);
					dealDTO.setIdFile(1L);

					try {
						for (IFileDealValidator fileDealValidator : getDataFileInDTO().getValidators()) {
							fileDealValidator.validateDeal(dealDTO, dataFileOutDTO.getCountLine());
						}

					} catch (FileDealValidatorException e) {
						LOG.error(e.getMessage());
						registerError(dataFileOutDTO, dealDTO, e.getMessage());
						continue;
					}

					PsFile psFile = new PsFile();
					psFile.setId(dealDTO.getIdFile());

					final PsDealData psDealData = new PsDealData();
					psDealData.setDealId(new BigInteger(dealDTO.getId()));
					psDealData.setIsoSource(dealDTO.getIsoSource());
					psDealData.setIsoTarget(dealDTO.getIsoTarget());
					try {
						psDealData.setDate(SDF_FORMAT.parse(dealDTO.getDate()));
					} catch (ParseException e) {
						LOG.error(e.getMessage(), e);
						registerError(dataFileOutDTO, dealDTO, e.getMessage());
						continue;
					} catch (NumberFormatException e) {
						LOG.error(e.getMessage(), e);
						registerError(dataFileOutDTO, dealDTO, e.getMessage());
						continue;
					}

					psDealData.setAmount(new BigDecimal(dealDTO.getAmount()));
					psDealData.setPsFile(psFile);

					int i = 1;
					em.createNativeQuery(sql.toString()).setParameter(i++, dealDTO.getLine())
							.setParameter(i++, psDealData.getPsFile().getId()).setParameter(i++, psDealData.getDealId())
							.setParameter(i++, psDealData.getIsoSource()).setParameter(i++, psDealData.getIsoTarget())
							.setParameter(i++, psDealData.getDate(), TemporalType.TIMESTAMP)
							.setParameter(i++, psDealData.getAmount()).executeUpdate();

				} catch (Exception e) {
					LOG.error(e.getMessage(), e);
				}
			}

			try {
				transaction.commit();
			} catch (Exception e) {
				if (transaction != null && transaction.isActive()) {
					transaction.rollback();
				}
			}

			return dataFileOutDTO;

		} finally {
			if (em != null) {
				LOG.info("Thread -> " + Thread.currentThread());
				em.close();
			}
		}
	}

}
