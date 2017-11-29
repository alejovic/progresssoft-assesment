package com.progresssoft.deal.control.persistence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.naming.OperationNotSupportedException;
import javax.persistence.EntityManagerFactory;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.progresssoft.deal.control.reader.BaseReaderFile;
import com.progresssoft.deal.control.reader.ReadLineIteratorImpl;
import com.progresssoft.deal.entity.dto.DataFileInDTO;
import com.progresssoft.deal.entity.dto.DataFileOutDTO;
import com.progresssoft.deal.resource.BaseTest;

public class DealPersistenceTest extends BaseTest {

	private EntityManagerFactory emf;

	Logger LOG = LoggerFactory.getLogger(this.getClass());

	@Before
	public void setUp() {
		super.setUp();
		emf = getEmfMySQL();
	}

	@Test
	public void persistence_Test() {
		DataFileInDTO dataFileInDTO = new DataFileInDTO();
		dataFileInDTO.setFileDTO(getFileCleanDTO());

		// lines
		List<String> lines = null;
		BaseReaderFile reader = new ReadLineIteratorImpl();
		try {
			lines = reader.getReadAllLines(dataFileInDTO.getFileDTO());
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
			fail(e.getMessage());
		}

		long startTime = System.nanoTime();
		// persistence
		BaseDealPersistence dealPersistence = null;
		try {
			dealPersistence = DealPersistenceFactory.getInstance(DealPersistenceEnum.MULTIROW);
			dealPersistence.init(emf, dataFileInDTO, lines);
		} catch (OperationNotSupportedException e) {
			LOG.error(e.getMessage(), e);
			fail(e.getMessage());
		}

		DataFileOutDTO dataFileOutDTO = null;
		// https://stackoverflow.com/questions/13759418/com-mysql-jdbc-packettoobigexception
		try {
			// MYSQL ->
			// SHOW VARIABLES LIKE 'max_allowed_packet'
			// SET GLOBAL max_allowed_packet=16777216;
			dataFileOutDTO = dealPersistence.call();
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			fail(e.getMessage());
		}

		LOG.info("\n" + dataFileOutDTO.getInfoLoad());

		long endTime = System.nanoTime();
		long elapsedTimeInMillis = TimeUnit.MILLISECONDS.convert((endTime - startTime), TimeUnit.NANOSECONDS);
		System.out.println("Total elapsed time: " + elapsedTimeInMillis + " ms");
		
		int expected = 100290;
		assertEquals(expected , dataFileOutDTO.getCountLine());
	}

}
