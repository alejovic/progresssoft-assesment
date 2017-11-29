package com.progresssoft.deal.boundary.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.naming.OperationNotSupportedException;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.progresssoft.deal.control.persistence.BaseDealPersistence;
import com.progresssoft.deal.control.persistence.DealPersistenceEnum;
import com.progresssoft.deal.control.persistence.DealPersistenceFactory;
import com.progresssoft.deal.control.reader.IReader;
import com.progresssoft.deal.entity.dto.DataFileInDTO;
import com.progresssoft.deal.entity.dto.DataFileOutDTO;
import com.progresssoft.deal.entity.dto.FileDTO;

@Stateless(name = "ExecutorServiceBean", mappedName = "ProgressSoft-EJB-ExecutorServiceBean")
public class TaskServiceBean {

	protected Logger LOG = LoggerFactory.getLogger(this.getClass());

	@PersistenceUnit(unitName = "progressoft-assesment-mysql-non-jta")
	EntityManagerFactory emf;

	@Resource
	ExecutorService executorService;
	
	// ManagedExecutorService executorService;

	@Inject
	@Default
	IReader reader;

	public TaskServiceBean() {
	}

	// stub - test
	public TaskServiceBean(EntityManagerFactory emf, ExecutorService executorService, IReader reader) {
		super();
		this.emf = emf;
		this.executorService = executorService;
		this.reader = reader;
	}

	// TODO: Parameter
	private int getSizeSplitList() {
		int TARGET_TASK = 2000;
		return TARGET_TASK;
	}

	public DataFileOutDTO executor(DataFileInDTO dataFileInDTO) throws DealServiceException {
		long startTime = System.nanoTime();
		final FileDTO fileDTO = dataFileInDTO.getFileDTO();

		List<List<String>> lstLinesRead = null;
		try {
			lstLinesRead = reader.getReadAllLinesBySize(fileDTO, getSizeSplitList());
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
			throw new DealServiceException("ERROR: Reading File - " + e.getMessage());
		}

		List<Callable<DataFileOutDTO>> tasks = new ArrayList<>();

		for (List<String> lines : lstLinesRead) {
			BaseDealPersistence dealPersistence;
			try {
				dealPersistence = DealPersistenceFactory.getInstance(DealPersistenceEnum.MULTIROW);
			} catch (OperationNotSupportedException e) {
				LOG.error(e.getMessage(), e);
				throw new DealServiceException(
						"ERROR: Reading Implementation DealPersistenceFactory - " + e.getMessage());
			}
			dealPersistence.init(emf, dataFileInDTO, lines);
			tasks.add(dealPersistence);
		}
		LOG.info(" # Tasks - " + tasks.size());

		List<Future<DataFileOutDTO>> resultTask = null;

		try {
			resultTask = executorService.invokeAll(tasks);
		} catch (InterruptedException e) {
			LOG.error(e.getMessage(), e);
		}

		long countLine = 0;
		long countError = 0;
		Map<Long, String[]> errorLines = new HashMap<Long, String[]>();
		Map<String, Long> isoSourceTx = new HashMap<String, Long>();
		Map<String, Long> isoTargeTx = new HashMap<String, Long>();

		for (Future<DataFileOutDTO> future : resultTask) {
			try {
				DataFileOutDTO dataFileOutDTO = future.get();
				LOG.info("Future result is - " + " - " + dataFileOutDTO + "; And Task done is " + future.isDone());
				countLine = countLine + dataFileOutDTO.getCountLine();
				countError = countError + dataFileOutDTO.getCountError();
				errorLines.putAll(dataFileOutDTO.getErrorLines());
				isoSourceTx.putAll(dataFileOutDTO.getIsoSourceTx());
				isoTargeTx.putAll(dataFileOutDTO.getIsoTargetTx());
			} catch (InterruptedException | ExecutionException e) {
				LOG.error(e.getMessage(), e);
			}
		}

		DataFileOutDTO dataFileOutDTO = new DataFileOutDTO();
		dataFileOutDTO.setId(dataFileInDTO.getFileDTO().getId());
		dataFileOutDTO.setCountError(countError);
		dataFileOutDTO.setCountLine(countLine);
		dataFileOutDTO.getErrorLines().putAll(errorLines);
		dataFileOutDTO.getIsoSourceTx().putAll(isoTargeTx);
		dataFileOutDTO.getIsoTargetTx().putAll(isoTargeTx);

		long endTime = System.nanoTime();
		long elapsedTimeInMillis = TimeUnit.MILLISECONDS.convert((endTime - startTime), TimeUnit.NANOSECONDS);
		dataFileOutDTO.setTimeLoad(elapsedTimeInMillis);

		// executorService.shutdown();

		return dataFileOutDTO;

	}

}
