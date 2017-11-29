package com.progresssoft.deal.control.dao.strategy;

import java.text.SimpleDateFormat;
import java.util.List;

import javax.persistence.EntityManagerFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.progresssoft.deal.entity.dto.DataFileInDTO;
import com.progresssoft.deal.entity.dto.DataFileOutDTO;
import com.progresssoft.deal.entity.dto.DealDTO;

public abstract class BaseDealPersistence implements IDealPersistence {

	protected final Logger LOG = LoggerFactory.getLogger(this.getClass());
	protected static final String DEFAULT_SEPARATOR = ",";
	protected static final SimpleDateFormat SDF_FORMAT = new SimpleDateFormat("YYYY/MM/dd HH:mm:ss");

	private EntityManagerFactory emf;
	private List<String> lines;
	private DataFileInDTO dataFileInDTO;

	public BaseDealPersistence(EntityManagerFactory emf, DataFileInDTO dataFileInDTO, List<String> lines) {
		this.emf = emf;
		this.lines = lines;
		this.dataFileInDTO = dataFileInDTO;
	}
	
	protected void registerError(DataFileOutDTO dataFileDTO, DealDTO dealDTO, String message) {
		String[] srtMessage = new String[] { dealDTO.toString(), message };
		dataFileDTO.addErrorLines(srtMessage);
		dataFileDTO.addCountError();
	}

	protected EntityManagerFactory getEmf() {
		return emf;
	}

	protected void setEmf(EntityManagerFactory emf) {
		this.emf = emf;
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
