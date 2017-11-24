package com.progresssoft.deal.boundary.service.reader;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.persistence.EntityManagerFactory;

import org.apache.commons.collections4.ListUtils;

import com.progresssoft.deal.boundary.service.runnable.DataDealCallable;
import com.progresssoft.deal.entity.dto.DataFileInDTO;
import com.progresssoft.deal.entity.dto.DataFileOutDTO;

public class ReadFileConcurrency extends BaseReadFile implements Serializable {

	private static final long serialVersionUID = 1L;

	public ReadFileConcurrency(EntityManagerFactory emf) {
		super(emf);
	}

	@Override
	DataFileOutDTO readFile(DataFileInDTO dataFileInDTO, List<String> readAllLines) {
		final ExecutorService executor = Executors.newFixedThreadPool(10);
		int TARGET_TASK = 2000;
		List<List<String>> subLists = ListUtils.partition(readAllLines, TARGET_TASK);

		List<Callable<DataFileOutDTO>> tasks = new ArrayList<Callable<DataFileOutDTO>>();

		for (List<String> subList : subLists) {
			DataDealCallable callable = new DataDealCallable(emf, subList, dataFileInDTO);
			tasks.add(callable);
		}
		LOG.info(" # Tasks - " + tasks.size());

		List<Future<DataFileOutDTO>> resultList = null;
		
		try {
			resultList = executor.invokeAll(tasks);
		} catch (InterruptedException e) {
			LOG.error(e.getMessage(), e);
		}

		long countLine = 0;
		long countError = 0;
		Map<Long, String[]> errorLines = new HashMap<Long, String[]>();
		Map<String, Long> isoSourceTx = new HashMap<String, Long>();
		Map<String, Long> isoTargeTx = new HashMap<String, Long>();
		
		for (Future<DataFileOutDTO> future : resultList) {
			try {
				LOG.info("Future result is - " + " - " + future.get() + "; And Task done is " + future.isDone());
				countLine = countLine + future.get().getCountLine();
				countError = countError + future.get().getCountError();
				errorLines.putAll(future.get().getErrorLines());
				isoSourceTx.putAll(future.get().getIsoSourceTx());
				isoTargeTx.putAll(future.get().getIsoTargetTx());
				
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

		executor.shutdown();
		
		return dataFileOutDTO;
	}

}
