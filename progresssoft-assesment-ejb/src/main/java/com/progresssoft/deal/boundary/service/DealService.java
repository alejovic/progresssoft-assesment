package com.progresssoft.deal.boundary.service;

import java.util.List;

import javax.ejb.Local;

import com.progresssoft.deal.control.validator.IFileDealValidator;
import com.progresssoft.deal.entity.dto.DataFileInDTO;
import com.progresssoft.deal.entity.dto.DataFileOutDTO;

@Local
public interface DealService {

	public List<IFileDealValidator> getDealValidatorsAvailable() throws DealServiceException;
	
	public DataFileOutDTO readFile(DataFileInDTO dataFileInDTO) throws DealServiceException;
	
	public void processErrorDeal(DataFileOutDTO dataFileOutDTO) throws DealServiceException;
	
	public void processDealTx(DataFileOutDTO dataFileOutDTO) throws DealServiceException;

}