package com.progresssoft.deal.control.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Calendar;
import java.util.Random;

import javax.persistence.EntityManagerFactory;

import org.junit.Before;
import org.junit.Test;

import com.progresssoft.deal.entity.model.PsFile;
import com.progresssoft.deal.resource.BaseTest;

public class DealDAOSaveTest extends BaseTest {

	private EntityManagerFactory emf;
	private IEntityDao<PsFile> entityDao;

	@Before
	public void setUp() {
		super.setUp();
		emf = getEmfMySQL();
		entityDao = new BaseEntityDAO<PsFile>(emf, PsFile.class);
	}

	@Test
	public void saveDAO_Test() {
		PsFile psFile = new PsFile();
		psFile.setName("FileTestByJunit");
		psFile.setDate(Calendar.getInstance().getTime());

		try {
			entityDao.save(psFile);
		} catch (PersistenceDAOException e) {
			LOG.error(e.getMessage(), e);
			fail(e.getMessage());
		}

		assertEquals(true, psFile.getId() != 0);

	}

	@Test(expected = PersistenceDAOException.class)
	public void saveDAOException_Test() throws PersistenceDAOException {
		PsFile psFile = new PsFile();
		psFile.setId(1);
		psFile.setName("FileTestByJunit");
		psFile.setDate(Calendar.getInstance().getTime());
		entityDao.save(psFile);
	}

	@Test(expected = PersistenceDAOException.class)
	public void saveDAONameNull_Test() throws PersistenceDAOException {
		PsFile psFile = new PsFile();
		psFile.setDate(Calendar.getInstance().getTime());
		entityDao.save(psFile);
	}

}
