package com.progresssoft.deal.boundary.facade;

import javax.ejb.Local;

import com.progresssoft.deal.entity.dto.DataFileInDTO;
import com.progresssoft.deal.entity.dto.DataFileOutDTO;

@Local
public interface DealFacade {

	public void loadDealValidator(DataFileInDTO dataFileInDTO) throws BusinessException;

	public DataFileOutDTO processDataFileDeal(DataFileInDTO dataFileInDTO) throws BusinessException;
}
