package com.progresssoft.deal.boundary.facade;

import static org.junit.Assert.fail;

import java.util.concurrent.ExecutorService;

import org.junit.Before;
import org.junit.Test;

import com.progresssoft.deal.boundary.service.DealServiceBean;
import com.progresssoft.deal.boundary.service.DealServiceException;
import com.progresssoft.deal.boundary.service.TaskServiceBean;
import com.progresssoft.deal.control.reader.IReader;
import com.progresssoft.deal.control.reader.ReadNIOFileImpl;
import com.progresssoft.deal.entity.dto.DataFileInDTO;
import com.progresssoft.deal.entity.dto.DataFileOutDTO;
import com.progresssoft.deal.resource.BaseTest;

public class DealFacadeTest extends BaseTest {

	private DealFacadeBean facade;

	@Before
	public void setUp() {
		super.setUp();
		facade = new DealFacadeBean();

		ExecutorService executorService = java.util.concurrent.Executors.newFixedThreadPool(10);
		IReader reader = new ReadNIOFileImpl();
		TaskServiceBean taskServiceBean = new TaskServiceBean(getEmfMySQL(), executorService, reader);

		facade.dealService = new DealServiceBean(getEmfMySQL(), taskServiceBean);
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
