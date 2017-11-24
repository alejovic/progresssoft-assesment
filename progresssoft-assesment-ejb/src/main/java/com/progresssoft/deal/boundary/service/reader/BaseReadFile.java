package com.progresssoft.deal.boundary.service.reader;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.persistence.EntityManagerFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.progresssoft.deal.boundary.service.DealServiceException;
import com.progresssoft.deal.entity.dto.DataFileInDTO;
import com.progresssoft.deal.entity.dto.DataFileOutDTO;
import com.progresssoft.deal.entity.dto.FileDTO;

public abstract class BaseReadFile implements Serializable {

	private static final long serialVersionUID = 1L;
	protected Logger LOG = LoggerFactory.getLogger(this.getClass());
	EntityManagerFactory emf;

	public BaseReadFile(EntityManagerFactory emf) {
		super();
		this.emf = emf;
	}

	abstract DataFileOutDTO readFile(DataFileInDTO dataFileDTO, List<String> readAllLines);

	public DataFileOutDTO executor(DataFileInDTO dataFileInDTO) throws DealServiceException {
		long startTime = System.nanoTime();
		DataFileOutDTO dataFileOutDTO = null;
		try {
			List<String> readAllLines = getReadAllLines(dataFileInDTO);
			dataFileOutDTO = readFile(dataFileInDTO, readAllLines);
			dataFileOutDTO.setId(dataFileInDTO.getFileDTO().getId());
			LOG.info("Process -> Lines Read -> " + dataFileOutDTO.getCountLine());
			LOG.info("Process -> Error Lines Read -> " + dataFileOutDTO.getCountError());
		} catch(IOException e) {
			LOG.error(e.getMessage(), e);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		} finally {
			long endTime = System.nanoTime();
			long elapsedTimeInMillis = TimeUnit.MILLISECONDS.convert((endTime - startTime), TimeUnit.NANOSECONDS);
			LOG.info("Thread -> Total elapsed time: " + elapsedTimeInMillis + " ms");

		}

		return dataFileOutDTO;

	}

	private List<String> getReadAllLines(DataFileInDTO dataFileDTO) throws IOException {
		final FileDTO fileDTO = dataFileDTO.getFileDTO();
		Path file = Paths.get(fileDTO.getPath() + File.separator + fileDTO.getName());
		List<String> readAllLines = Files.readAllLines(file, StandardCharsets.UTF_8);
		readAllLines.remove(0);
		return readAllLines;
	}

}
