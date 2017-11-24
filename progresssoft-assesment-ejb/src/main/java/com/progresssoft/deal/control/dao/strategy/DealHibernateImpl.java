package com.progresssoft.deal.control.dao.strategy;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.jdbc.Work;

import com.progresssoft.deal.control.validator.FileDealValidatorException;
import com.progresssoft.deal.control.validator.IFileDealValidator;
import com.progresssoft.deal.entity.dto.DataFileInDTO;
import com.progresssoft.deal.entity.dto.DataFileOutDTO;
import com.progresssoft.deal.entity.dto.DealDTO;

public class DealHibernateImpl extends BaseDealPersistence {

	public DealHibernateImpl(EntityManagerFactory emf, DataFileInDTO dataFileInDTO, List<String> lines) {
		super(emf, dataFileInDTO, lines);
	}

	public DataFileOutDTO execute() {
		long startTime = System.nanoTime();
		List<String> lines = null;
		DataFileOutDTO dataFileOutDTO = null;

		Session session = null;
		EntityManager em = null;

		try {
			lines = getLines();
			dataFileOutDTO = new DataFileOutDTO();

			int entityCount = lines.size();

			em = getEmf().createEntityManager();
			session = em.unwrap(Session.class);

			Transaction transaction = session.beginTransaction();

			long endTime = System.nanoTime();
			long elapsedTimeInMillis = TimeUnit.MILLISECONDS.convert((endTime - startTime), TimeUnit.NANOSECONDS);
			LOG.info("Thread Tx Begin-> " + Thread.currentThread().getName() + " Total elapsed time: "
					+ elapsedTimeInMillis + " ms");

			final List<String> lstString = new ArrayList<>();
			for (int index = 0; index < entityCount; index++) {

				dataFileOutDTO.addCountLine();

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

					LOG.debug("Thread -> " + Thread.currentThread() + " Line -> " + dealDTO);

					try {
						for (IFileDealValidator fileDealValidator : getDataFileInDTO().getValidators()) {
							fileDealValidator.validateDeal(dealDTO, dataFileOutDTO.getCountLine());
						}

					} catch (FileDealValidatorException e) {
						LOG.error(e.getMessage());
						registerError(dataFileOutDTO, dealDTO, e.getMessage());
						continue;
					}

					StringBuilder sql = new StringBuilder();
					sql.append("INSERT INTO ");
					sql.append(
							"	progresssoft.ps_deal_data (FK_ID_FILE, DEAL_ID, ISO_SOURCE, ISO_TARGET, DATE, AMOUNT) ");
					sql.append("	VALUES ");
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
					lstString.add(sql.toString());
				} catch (Exception e) {
					LOG.error(e.getMessage(), e);
				}
			}

			session.doWork(new Work() {
				@Override
				public void execute(Connection conn) throws SQLException {
					PreparedStatement pstmt = null;
					try {
						int batch = 1000;
						int index = 0;
						for (String sql : lstString) {
							pstmt = conn.prepareStatement(sql);
							pstmt.addBatch();
							if (index % batch == 0) {
								pstmt.executeBatch();
							}
							index++;
						}

						pstmt.executeBatch();
					} finally {
						pstmt.close();
					}
				}
			});

			startTime = System.nanoTime();
			transaction.commit();
			endTime = System.nanoTime();
			elapsedTimeInMillis = TimeUnit.MILLISECONDS.convert((endTime - startTime), TimeUnit.NANOSECONDS);
			LOG.debug("Thread Tx Commit-> " + Thread.currentThread().getName() + " Total elapsed time: "
					+ elapsedTimeInMillis + " ms");

			return dataFileOutDTO;
		} finally {
			LOG.debug("Thread -> " + Thread.currentThread());
			session.close();
		}
	}

}
