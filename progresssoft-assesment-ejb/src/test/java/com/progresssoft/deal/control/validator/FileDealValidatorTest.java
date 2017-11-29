package com.progresssoft.deal.control.validator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.persistence.EntityManagerFactory;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.progresssoft.deal.entity.dto.DealDTO;
import com.progresssoft.deal.resource.BaseTest;

public class FileDealValidatorTest extends BaseTest {

	private static final String PATH_CSV_TEST = "/DEAL_SOURCE_TEST_VALIDATOR.csv";
	
	private IFileDealValidator fileDealValidator;
	EntityManagerFactory emf;

	Logger LOG = LoggerFactory.getLogger(this.getClass());

	private static final String DEFAULT_SEPARATOR = ",";

	@Before
	public void setUp() {
		super.setUp();
		
		URL url = this.getClass().getResource(PATH_CSV_TEST);
		
		File file = null;
		try {
			file = new File(url.toURI());
		} catch (URISyntaxException e1) {
			e1.printStackTrace();
		}
		
		Path path = Paths.get(file.getPath());

		byte[] data = null;
		try {
			data = Files.readAllBytes(path);
		} catch (IOException e) {
			fail(e.getMessage());
		}
		when(getFileDTO().getFile()).thenReturn(data);
		when(getFileDTO().getName()).thenReturn(PATH_CSV_TEST);
		
		emf = getEmfMySQL();
	}

	private void readFileAndValidate(IFileDealValidator fileDealValidator, int expectedLine, int expectedError) {
		ByteArrayInputStream bais = new ByteArrayInputStream(getFileDTO().getFile());
		InputStreamReader isr = new InputStreamReader(bais);
		BufferedReader br = new BufferedReader(isr);
		long countError = 0;
		long countLine = 0;

		try {
			// escape header
			String line = br.readLine();
			countLine = countLine + 1;

			while ((line = br.readLine()) != null) {
				countLine = countLine + 1;

				String[] fields = line.split(DEFAULT_SEPARATOR);
				try {
					DealDTO dealDTO = new DealDTO();
					dealDTO.setId(fields[0]);
					dealDTO.setIsoSource(fields[1]);
					dealDTO.setIsoTarget(fields[2]);
					dealDTO.setDate(fields[3]);
					dealDTO.setAmount(fields[4]);
					fileDealValidator.validateDeal(dealDTO, countLine);
				} catch (FileDealValidatorException e) {
					LOG.error(e.getMessage());
					countError = countError + 1;
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}

		assertEquals(countLine, expectedLine);
		assertEquals(countError, expectedError);
	}

	@Test
	public void validateNullEmpty_Test() {
		fileDealValidator = new NullValidatorImpl(emf);
		readFileAndValidate(fileDealValidator, 16, 1);
	}

	@Test
	public void validateISOSourceCode_Test() {
		fileDealValidator = new ISOCodeSourceValidatorImpl(emf);
		readFileAndValidate(fileDealValidator, 16, 0);
	}

	@Test
	public void validateISOTargetCode_Test() {
		fileDealValidator = new ISOCodeTargetValidatorImpl(emf);
		readFileAndValidate(fileDealValidator, 16, 3);
	}

	@Test
	public void validateTypeFormat_Test() {
		fileDealValidator = new TypeFormatValidatorImpl(emf);
		readFileAndValidate(fileDealValidator, 16, 6);
	}

	@Test
	public void validateUniqueDeal_Test() {
		fileDealValidator = new UniqueDealValidatorImpl(emf);
		readFileAndValidate(fileDealValidator, 16, 0);
	}

}
