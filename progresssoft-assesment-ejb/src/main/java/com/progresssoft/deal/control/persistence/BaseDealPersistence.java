package com.progresssoft.deal.control.persistence;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.progresssoft.deal.entity.dto.DataFileInDTO;
import com.progresssoft.deal.entity.dto.DataFileOutDTO;
import com.progresssoft.deal.entity.dto.DealDTO;

public abstract class BaseDealPersistence implements IDealPersistence, Callable<DataFileOutDTO> {

	private static final long serialVersionUID = 1L;
	protected final Logger LOG = LoggerFactory.getLogger(this.getClass());
	protected static final String DEFAULT_SEPARATOR = ",";
	protected static final SimpleDateFormat SDF_FORMAT = new SimpleDateFormat("YYYY/MM/dd HH:mm:ss");

	private EntityManagerFactory emf;
	private List<String> lines;
	private DataFileInDTO dataFileInDTO;

	public BaseDealPersistence() {
	}

	public void init(EntityManagerFactory emf, DataFileInDTO dataFileInDTO, List<String> lines) {
		this.emf = emf;
		this.lines = lines;
		this.dataFileInDTO = dataFileInDTO;
	}

	@Override
	public DataFileOutDTO call() throws Exception {
		String threadName = Thread.currentThread().getName();
		long startTime = System.nanoTime();
		long elapsedTimeInMillis = 0;
		try {
			LOG.debug("initializating Runnable, reading Thread -> " + threadName + "::" + new Date());
			DataFileOutDTO dataFileOutDTO = execute();

			long endTime = System.nanoTime();
			elapsedTimeInMillis = TimeUnit.MILLISECONDS.convert((endTime - startTime), TimeUnit.NANOSECONDS);
			dataFileOutDTO.setTimeLoad(elapsedTimeInMillis);
			
			return dataFileOutDTO;
		} finally {
			LOG.debug("Thread Executor-> " + Thread.currentThread().getName() + " Total elapsed time: "
					+ elapsedTimeInMillis + " ms");
		}
	}

	protected void registerError(DataFileOutDTO dataFileDTO, DealDTO dealDTO, String message) {
		String[] srtMessage = new String[] { dealDTO.toString(), message };
		dataFileDTO.addErrorLines(srtMessage);
		dataFileDTO.addCountError();
	}

	protected EntityManager createEntityManager() {
		return emf.createEntityManager();
	}

	protected List<String> getLines() {
		return lines;
	}

	protected void setLines(List<String> lines) {
		this.lines = lines;
	}

	public DataFileInDTO getDataFileInDTO() {
		return dataFileInDTO;
	}

	public void setDataFileInDTO(DataFileInDTO dataFileInDTO) {
		this.dataFileInDTO = dataFileInDTO;
	}

}
