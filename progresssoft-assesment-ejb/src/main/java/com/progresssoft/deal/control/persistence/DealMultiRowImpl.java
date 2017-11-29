package com.progresssoft.deal.control.persistence;

import java.text.MessageFormat;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import com.progresssoft.deal.control.validator.FileDealValidatorException;
import com.progresssoft.deal.control.validator.IFileDealValidator;
import com.progresssoft.deal.entity.dto.DataFileOutDTO;
import com.progresssoft.deal.entity.dto.DealDTO;

public class DealMultiRowImpl extends BaseDealPersistence {

	private static final long serialVersionUID = 1L;

	public DealMultiRowImpl() {
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

			long startTime = System.nanoTime();
			em = createEntityManager();
			transaction = em.getTransaction();
			transaction.begin();
			long endTime = System.nanoTime();
			long elapsedTimeInMillis = TimeUnit.MILLISECONDS.convert((endTime - startTime), TimeUnit.NANOSECONDS);
			LOG.debug("Thread Tx Begin-> " + Thread.currentThread().getName() + " Total elapsed time: "
					+ elapsedTimeInMillis + " ms");

			StringBuilder sql = new StringBuilder();
			sql.append("INSERT INTO ");
			sql.append("	progresssoft.ps_deal_data (FK_ID_FILE, DEAL_ID, ISO_SOURCE, ISO_TARGET, DATE, AMOUNT) ");
			sql.append("	VALUES ");

			for (int index = 0; index < entityCount; index++) {

				dataFileOutDTO.addCountLine();

				try {
					String line = lines.get(index);
					String[] fields = line.split(DEFAULT_SEPARATOR);

					final DealDTO dealDTO = new DealDTO();

					synchronized (this) {
						dealDTO.setId(fields[0]);
						dealDTO.setIsoSource(fields[1]);
						dealDTO.setIsoTarget(fields[2]);
						dealDTO.setDate(fields[3]);
						dealDTO.setAmount(fields[4]);
						dealDTO.setIdFile(1L);

						// LOG.debug("Thread -> " + Thread.currentThread() + "
						// Line -> " + dealDTO);

						try {

							for (IFileDealValidator fileDealValidator : getDataFileInDTO().getValidators()) {
								fileDealValidator.validateDeal(dealDTO, dataFileOutDTO.getCountLine());
							}

						} catch (FileDealValidatorException e) {
							LOG.error(e.getMessage());
							registerError(dataFileOutDTO, dealDTO, e.getMessage());
							continue;
						}
					}

					sql.append("	(");
					sql.append(MessageFormat.format("''{0}''", dealDTO.getIdFile()));
					sql.append(",");
					sql.append(MessageFormat.format("''{0}''", dealDTO.getId()));
					sql.append(",");
					sql.append(MessageFormat.format("''{0}''", dealDTO.getIsoSource()));
					sql.append(",");
					sql.append(MessageFormat.format("''{0}''", dealDTO.getIsoTarget()));
					sql.append(",");
					sql.append(MessageFormat.format("''{0}''", dealDTO.getDate()));
					sql.append(",");
					sql.append(MessageFormat.format("''{0}''", dealDTO.getAmount()));
					sql.append("	)");
					sql.append(",");

					dataFileOutDTO.addIsoSourceTx(dealDTO.getIsoSource());
					dataFileOutDTO.addIsoTargetTx(dealDTO.getIsoTarget());
				} catch (Exception e) {
					LOG.error(e.getMessage(), e);
				}
			}

			sql.delete(sql.length() - 1, sql.length());
			Query query = em.createNativeQuery(sql.toString());

			startTime = System.nanoTime();
			query.executeUpdate();

			try {
				transaction.commit();
			} catch (Exception e) {
				if (transaction != null && transaction.isActive()) {
					transaction.rollback();
				}
			}

			endTime = System.nanoTime();
			elapsedTimeInMillis = TimeUnit.MILLISECONDS.convert((endTime - startTime), TimeUnit.NANOSECONDS);
			LOG.debug("Thread Tx Commit-> " + Thread.currentThread().getName() + " Total elapsed time: "
					+ elapsedTimeInMillis + " ms");

			return dataFileOutDTO;

		} finally {
			LOG.debug("Thread -> " + Thread.currentThread());
			if (em != null) {
				em.close();
			}
		}

	}

}
