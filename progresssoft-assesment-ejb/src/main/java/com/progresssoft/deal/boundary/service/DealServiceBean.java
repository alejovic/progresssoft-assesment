package com.progresssoft.deal.boundary.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.ejb.Stateless;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.progresssoft.deal.boundary.service.reader.BaseReadFile;
import com.progresssoft.deal.boundary.service.reader.ReadFileConcurrency;
import com.progresssoft.deal.control.dao.BaseEntityDAO;
import com.progresssoft.deal.control.dao.IEntityDao;
import com.progresssoft.deal.control.dao.IPsDealTx;
import com.progresssoft.deal.control.dao.IPsDealValidator;
import com.progresssoft.deal.control.dao.PersistenceDAOException;
import com.progresssoft.deal.control.dao.PsDealTxDAO;
import com.progresssoft.deal.control.dao.PsDealValidatorDAO;
import com.progresssoft.deal.control.validator.IFileDealValidator;
import com.progresssoft.deal.entity.dto.DataFileInDTO;
import com.progresssoft.deal.entity.dto.DataFileOutDTO;
import com.progresssoft.deal.entity.dto.FileDTO;
import com.progresssoft.deal.entity.model.PsDealDataError;
import com.progresssoft.deal.entity.model.PsDealTx;
import com.progresssoft.deal.entity.model.PsDealValidator;
import com.progresssoft.deal.entity.model.PsFile;

@Stateless(name = "DealServiceBean", mappedName = "ProgressSoft-EJB-DealServiceBean")
public class DealServiceBean implements DealService {

	@PersistenceUnit
	EntityManagerFactory emf;

	private Logger LOG = LoggerFactory.getLogger(this.getClass());

	public DealServiceBean() {
	}

	public DealServiceBean(EntityManagerFactory emf) {
		this.emf = emf;
	}

	@Override
	public List<IFileDealValidator> getDealValidatorsAvailable() throws DealServiceException {

		List<IFileDealValidator> lstValidator = null;
		lstValidator = new ArrayList<IFileDealValidator>();

		final IPsDealValidator psDealValidatorDAO = new PsDealValidatorDAO(emf, PsDealValidator.class);
		List<PsDealValidator> lstDealValidators = null;
		try {
			lstDealValidators = psDealValidatorDAO.getDealValidatorActive();
		} catch (PersistenceDAOException e) {
			LOG.error(e.getMessage());
			throw new DealServiceException("ERROR: Getting Deal Validators" + e.getMessage());
		}
		for (PsDealValidator psDealValidator : lstDealValidators) {
			Class<?> clazz = null;
			try {
				clazz = Class.forName(psDealValidator.getClass_());
			} catch (ClassNotFoundException e) {
				LOG.error(e.getMessage());
				throw new DealServiceException("ERROR: Getting Deal Validators" + e.getMessage());
			}
			Constructor<?> ctor = null;
			try {
				ctor = clazz.getConstructor(EntityManagerFactory.class);
			} catch (NoSuchMethodException | SecurityException e) {
				LOG.error(e.getMessage());
				throw new DealServiceException("ERROR: Getting Deal Validators" + e.getMessage());
			}
			Object object = null;
			try {
				object = ctor.newInstance(new Object[] { emf });
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				LOG.error(e.getMessage());
				throw new DealServiceException("ERROR: Getting Deal Validators" + e.getMessage());
			}

			lstValidator.add((IFileDealValidator) object);
		}

		return lstValidator;

	}

	public DataFileOutDTO readFile(DataFileInDTO dataFileDTO) throws DealServiceException {
		long startTime = System.nanoTime();

		DataFileOutDTO dataFileOutDTO = null;
		final FileDTO fileDTO = dataFileDTO.getFileDTO();

		saveFileTemporary(fileDTO);

		savePsFile(fileDTO);

		try {
			BaseReadFile readFile = new ReadFileConcurrency(emf);
			dataFileOutDTO = readFile.executor(dataFileDTO);

		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}

		updatePsFile(dataFileOutDTO);
		
		long endTime = System.nanoTime();
		long elapsedTimeInMillis = TimeUnit.MILLISECONDS.convert((endTime - startTime), TimeUnit.NANOSECONDS);
		LOG.info("Thread -> Total elapsed time: " + elapsedTimeInMillis + " ms");

		dataFileOutDTO.setTimeLoad(elapsedTimeInMillis);
		return dataFileOutDTO;
	}

	@Override
	public void processErrorDeal(DataFileOutDTO dataFileOutDTO) throws DealServiceException {

		final IEntityDao<PsFile> psFileDAO = new BaseEntityDAO<PsFile>(emf, PsFile.class);
		final IEntityDao<PsDealDataError> psDealDataErrorDAO = new BaseEntityDAO<PsDealDataError>(emf,
				PsDealDataError.class);

		PsFile psFile = null;
		try {
			psFile = psFileDAO.find(dataFileOutDTO.getId());
		} catch (PersistenceDAOException e) {
			LOG.error(e.getMessage());
			throw new DealServiceException("ERROR: PsFile Not found" + e.getMessage());
		}

		if (psFile != null) {
			try {
				Map<Long, String[]> mapErrorLine = dataFileOutDTO.getErrorLines();
				Set<Entry<Long, String[]>> setErrorLine = mapErrorLine.entrySet();
				for (Entry<Long, String[]> entry : setErrorLine) {
					final PsDealDataError psDealDataError = new PsDealDataError();
					psDealDataError.setDate(psFile.getDate());
					psDealDataError.setLine(BigInteger.valueOf(entry.getKey()));
					psDealDataError.setDealError(entry.getValue()[0]);
					psDealDataError.setMesssageError(entry.getValue()[1]);
					psDealDataError.setPsFile(psFile);
					psDealDataErrorDAO.save(psDealDataError);
				}
			} catch (PersistenceDAOException e) {
				LOG.error(e.getMessage(), e);
				throw new DealServiceException("ERROR: " + e.getMessage());
			}
		}

	}

	@Override
	public void processDealTx(DataFileOutDTO dataFileOutDTO) throws DealServiceException {

		final IPsDealTx psDealTxDAO = new PsDealTxDAO(emf, PsDealTx.class);

		try {
			Map<String, Long> mapIsoSource = dataFileOutDTO.getIsoSourceTx();
			Set<Entry<String, Long>> eIsoSource = mapIsoSource.entrySet();
			for (Entry<String, Long> entry : eIsoSource) {
				psDealTxDAO.updatedDealTx(entry.getKey(), entry.getValue());
			}

		} catch (PersistenceDAOException e) {
			LOG.error(e.getMessage());
			throw new DealServiceException("ERROR: PsFile Not found" + e.getMessage());
		}

	}

	private void saveFileTemporary(FileDTO fileDTO) throws DealServiceException {
		final String PROGRESS_SOFT_TMP = "ProgressSoftTmp";
		final String TMP_PATH = System.getProperty("java.io.tmpdir");

		File directory = new File(TMP_PATH, PROGRESS_SOFT_TMP);
		directory.mkdirs();

		FileOutputStream fos = null;

		try {
			fileDTO.setPath(directory.getAbsolutePath());
			fos = new FileOutputStream(fileDTO.getPath() + File.separator + fileDTO.getName());
		} catch (FileNotFoundException e) {
			LOG.error(e.getMessage());
			throw new DealServiceException("ERROR: Creating TMP file" + e.getMessage());
		}

		try {
			fos.write(fileDTO.getFile());
			fos.close();
		} catch (IOException e) {
			LOG.error(e.getMessage());
			throw new DealServiceException("ERROR: Creating TMP file" + e.getMessage());
		}

	}

	private void savePsFile(FileDTO fileDTO) throws DealServiceException {
		final IEntityDao<PsFile> psFileDAO = new BaseEntityDAO<PsFile>(emf, PsFile.class);

		PsFile psFile = null;
		try {
			psFile = new PsFile();
			psFile.setDate(new Date());
			psFile.setName(fileDTO.getName());
			psFile.setStartLoad(new Date());
			psFile.setFail(BigInteger.ZERO);
			psFile.setTotal(BigInteger.ZERO);

			psFileDAO.save(psFile);

			fileDTO.setId(psFile.getId());
		} catch (PersistenceDAOException e) {
			LOG.error(e.getMessage(), e);
			throw new DealServiceException("ERROR: PsFile Not found" + e.getMessage());
		}
	}

	private void updatePsFile(DataFileOutDTO dataFileOutDTO) throws DealServiceException {

		final IEntityDao<PsFile> psFileDAO = new BaseEntityDAO<PsFile>(emf, PsFile.class);

		PsFile psFile = null;
		try {
			psFile = psFileDAO.find(dataFileOutDTO.getId());
		} catch (PersistenceDAOException e) {
			LOG.error(e.getMessage());
			throw new DealServiceException("ERROR: PsFile Not found" + e.getMessage());
		}

		try {
			psFile.setEndLoad(new Date());
			psFile.setTotal(BigInteger.valueOf(dataFileOutDTO.getCountLine()));
			psFile.setFail(BigInteger.valueOf(dataFileOutDTO.getCountError()));
			psFileDAO.update(psFile);
		} catch (PersistenceDAOException e) {
			LOG.error(e.getMessage(), e);
			throw new DealServiceException("ERROR: PsFile Not found" + e.getMessage());
		}
	}

}
