package com.progresssoft.deal.control.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.List;

import javax.persistence.EntityManagerFactory;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.progresssoft.deal.entity.model.PsFile;
import com.progresssoft.deal.resource.BaseTest;

public class DealDAOFindTest extends BaseTest {

	private EntityManagerFactory emf;
	private IEntityDao<PsFile> entityDao;

	Logger LOG = LoggerFactory.getLogger(this.getClass());

	@Before
	public void setUp() {
		super.setUp();
		emf = getEmfMySQL();
		entityDao = new BaseEntityDAO<PsFile>(emf, PsFile.class);
	}

	@SuppressWarnings("rawtypes")
	@Test
	public void findAllDAO_Test() {
		List result = null;

		try {
			result = entityDao.findAll();
		} catch (PersistenceDAOException e) {
			LOG.error(e.getMessage(), e);
			fail(e.getMessage());
		}

		boolean expected = result.size() > 0;
		assertEquals(true, expected);

	}

	@Test
	public void findDAO_Test() {
		LOG.info("test -> findDAO_Test");
		PsFile result = null;
		try {
			result = entityDao.find(1L);
		} catch (PersistenceDAOException e) {
			LOG.error(e.getMessage(), e);
			fail(e.getMessage());
		}

		String fileNameExpected = "FileTestByJunit";
		assertEquals(fileNameExpected, result.getName());

	}

}
