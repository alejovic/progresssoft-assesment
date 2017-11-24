package com.progresssoft.deal.boundary.service.reader;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManagerFactory;

import com.progresssoft.deal.boundary.service.runnable.DataDealCallable;
import com.progresssoft.deal.entity.dto.DataFileInDTO;
import com.progresssoft.deal.entity.dto.DataFileOutDTO;

public class ReadFileInLine extends BaseReadFile implements Serializable {

	private static final long serialVersionUID = 1L;

	public ReadFileInLine(EntityManagerFactory emf) {
		super(emf);
	}

	@Override
	DataFileOutDTO  readFile(DataFileInDTO dataFileDTO, List<String> readAllLines) {
		DataDealCallable callable = new DataDealCallable(emf, readAllLines, dataFileDTO);
		try {
			DataFileOutDTO dataFileOutDTO = callable.call();
			return dataFileOutDTO;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
		return null;
	}

}
