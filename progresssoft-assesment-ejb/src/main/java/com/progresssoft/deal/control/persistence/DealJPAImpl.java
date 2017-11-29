package com.progresssoft.deal.control.persistence;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import com.progresssoft.deal.control.validator.FileDealValidatorException;
import com.progresssoft.deal.control.validator.IFileDealValidator;
import com.progresssoft.deal.entity.dto.DataFileOutDTO;
import com.progresssoft.deal.entity.dto.DealDTO;
import com.progresssoft.deal.entity.model.PsDealData;
import com.progresssoft.deal.entity.model.PsDealTx;
import com.progresssoft.deal.entity.model.PsFile;

public class DealJPAImpl extends BaseDealPersistence {

	private static final long serialVersionUID = 1L;

	public DealJPAImpl() {
	}

	@Override
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

			for (int index = 0; index < entityCount; ++index) {

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

					PsFile psFile = new PsFile();
					psFile.setId(dealDTO.getIdFile());

					psDealData.setAmount(new BigDecimal(dealDTO.getAmount()));
					psDealData.setPsFile(psFile);
					em.persist(psDealData);

					Query query = em.createNamedQuery("PsDealTx.findByCode");
					query.setParameter("isoCode", psDealData.getIsoSource());
					PsDealTx dealTx = null;
					try {
						dealTx = (PsDealTx) query.getSingleResult();
					} catch (NoResultException e) {
						dealTx = null;
					}

					if (dealTx != null) {
						dealTx.setCount(dealTx.getCount().add(BigInteger.ONE));
						try {
							em.merge(dealTx);
						} catch (Exception e) {
							LOG.error(e.getMessage(), e);
							registerError(dataFileOutDTO, dealDTO, e.getMessage());
							continue;
						}

					}

				} catch (Exception e) {
					LOG.error(e.getMessage(), e);
				}
			}

			transaction.commit();

			return dataFileOutDTO;

		} finally {
			LOG.info("Thread -> " + Thread.currentThread());
			if (em != null) {
				em.close();
			}
		}
	}

}
