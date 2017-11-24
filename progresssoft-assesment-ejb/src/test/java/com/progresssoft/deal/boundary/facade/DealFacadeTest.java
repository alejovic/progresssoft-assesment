package com.progresssoft.deal.boundary.facade;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.progresssoft.deal.boundary.facade.DealFacadeBean;
import com.progresssoft.deal.boundary.service.DealServiceBean;
import com.progresssoft.deal.boundary.service.DealServiceException;
import com.progresssoft.deal.control.validator.IFileDealValidator;
import com.progresssoft.deal.entity.dto.DataFileInDTO;
import com.progresssoft.deal.entity.dto.DataFileOutDTO;
import com.progresssoft.deal.resource.BaseTest;

public class DealFacadeTest extends BaseTest {

	private DealFacadeBean facade;
	
	@Before
	public void setUp() {
		super.setUp();
		facade = new DealFacadeBean();
		facade.dealService = new DealServiceBean( getEmfMySQL());
	}

	@Test
	public void proceesDila_Test() throws DealServiceException {
		DataFileInDTO dataFileInDTO = new DataFileInDTO();
		dataFileInDTO.setFileDTO(getFileDTO());
		
		try {
			facade.loadDealValidator(dataFileInDTO);
		} catch (BusinessException e) {
			LOG.error(e.getMessage(), e);
			fail(e.getMessage());
		}

		DataFileOutDTO dataFileOutDTO = null;
		try {
			dataFileOutDTO = facade.processDataFileDeal(dataFileInDTO);
		} catch (BusinessException e) {
			LOG.error(e.getMessage(), e);
			fail(e.getMessage());
		}
		
		LOG.info(dataFileOutDTO.getInfoLoad());
	}

}
