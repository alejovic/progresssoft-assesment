package com.progresssoft.deal.boundary.service;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.progresssoft.deal.control.validator.IFileDealValidator;
import com.progresssoft.deal.entity.dto.DataFileInDTO;
import com.progresssoft.deal.entity.dto.DataFileOutDTO;
import com.progresssoft.deal.resource.BaseTest;

public class DealServiceTest extends BaseTest {

	private DealServiceBean service;

	@Before
	public void setUp() {
		super.setUp();
		service = new DealServiceBean();
		service.emf = getEmfMySQL();
	}

	@Test
	public void getDealValidators_Test() throws DealServiceException {
		List<IFileDealValidator> lstValidators = service.getDealValidatorsAvailable();
		assertEquals(5, lstValidators.size());
	}

	@Test(expected = DealServiceException.class)
	public void processDataFileDealWithoutFile_Test() throws DealServiceException {
		DataFileInDTO dataFileDTO = new DataFileInDTO();
		service.readFile(dataFileDTO);
		LOG.info(dataFileDTO.toString());

	}

	@Test(expected = DealServiceException.class)
	public void processDataFileDealWithoutValidator_Test() throws DealServiceException {
		DataFileInDTO dataFileDTO = new DataFileInDTO();
		dataFileDTO.setFileDTO(getFileDTO());
		service.readFile(dataFileDTO);
		LOG.info(dataFileDTO.toString());

	}

	@Test
	public void readFile_Test() throws DealServiceException {
		DataFileInDTO dataFileInDTO = new DataFileInDTO();
		dataFileInDTO.setFileDTO(getFileDTO());

		List<IFileDealValidator> lstValidator = service.getDealValidatorsAvailable();
		dataFileInDTO.setValidators(lstValidator);
		DataFileOutDTO dataFileOutDTO = null;
		dataFileOutDTO = service.readFile(dataFileInDTO);

		LOG.info(dataFileOutDTO.getInfoLoad());
	}

}
