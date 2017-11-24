package com.progresssoft.deal.boundary.facade;

import javax.ejb.Local;

import com.progresssoft.deal.entity.dto.DataFileInDTO;
import com.progresssoft.deal.entity.dto.DataFileOutDTO;

@Local
public interface DealFacade {

	public void loadDealValidator(DataFileInDTO dataFileDTO) throws BusinessException;

	public DataFileOutDTO processDataFileDeal(DataFileInDTO dataFileDTO) throws BusinessException;
}
