package com.progresssoft.deal.boundary.service.runnable;

import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import javax.persistence.EntityManagerFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.progresssoft.deal.control.dao.strategy.DealMultiRowImpl;
import com.progresssoft.deal.control.dao.strategy.IDealPersistence;
import com.progresssoft.deal.entity.dto.DataFileInDTO;
import com.progresssoft.deal.entity.dto.DataFileOutDTO;

public class DataDealCallable implements Callable<DataFileOutDTO> {

	private Logger LOG = LoggerFactory.getLogger(this.getClass());
	private EntityManagerFactory emf;
	private List<String> lines;
	private DataFileInDTO dataFileDTO;

	public DataDealCallable(EntityManagerFactory emf, List<String> lines, DataFileInDTO dataFileDTO) {
		super();
		this.emf = emf;
		this.lines = lines;
		this.dataFileDTO = dataFileDTO;
	}

	@Override
	public DataFileOutDTO call() throws Exception {
		String threadName = Thread.currentThread().getName();
		long startTime = System.nanoTime();
		try {
			LOG.debug("initializating Runnable, reading Thread -> " + threadName + "::" + new Date());
			// new DealNativeQueryImpl;DealMultiRowImpl DealJPAImpl, DealHibernateImpl
			final IDealPersistence dealPersistence = new DealMultiRowImpl(emf, dataFileDTO, lines);
			DataFileOutDTO dataFileOutDTO = dealPersistence.execute();
			return dataFileOutDTO;
		} finally {
			long endTime = System.nanoTime();
			long elapsedTimeInMillis = TimeUnit.MILLISECONDS.convert((endTime - startTime), TimeUnit.NANOSECONDS);
			LOG.debug("Thread Executor-> " + Thread.currentThread().getName() + " Total elapsed time: " + elapsedTimeInMillis + " ms");
		}
	}
}
