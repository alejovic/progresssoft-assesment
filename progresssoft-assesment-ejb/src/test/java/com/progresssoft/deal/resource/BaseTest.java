package com.progresssoft.deal.resource;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.progresssoft.deal.boundary.service.reader.ReadFileTest;
import com.progresssoft.deal.entity.dto.FileDTO;

public abstract class BaseTest {
	
	private static final String PROGRESSOFT_ASSESMENT_MYSQL_TEST = "progressoft-assesment-mysql-test";
	private static final String PROGRESSOFT_ASSESMENT_MONGO_TEST = "progressoft-assesment-mongo-test";
	private static final String PATH_CSV_TEST = "/DEAL_SOURCE_TEST.csv";

	private FileDTO fileDTO;

	protected Logger LOG = LoggerFactory.getLogger(this.getClass());
	
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		URL url = ReadFileTest.class.getResource(PATH_CSV_TEST);
		
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

		fileDTO = mock(FileDTO.class);
		when(fileDTO.getFile()).thenReturn(data);
		when(fileDTO.getName()).thenReturn("FileTmp.csv");
		when(fileDTO.getPath()).thenReturn("D:\\");
		when(fileDTO.getId()).thenReturn(1L);

	}
	
	public EntityManagerFactory getEmfMySQL(){
		EntityManagerFactory emf = Persistence.createEntityManagerFactory(PROGRESSOFT_ASSESMENT_MYSQL_TEST);
		return emf;
	}
	
	public EntityManagerFactory getEmfMongoDBL(){
		EntityManagerFactory emf = Persistence.createEntityManagerFactory(PROGRESSOFT_ASSESMENT_MONGO_TEST);
		return emf;
	}

	public FileDTO getFileDTO() {
		return fileDTO;
	}

	public void setFileDTO(FileDTO fileDTO) {
		this.fileDTO = fileDTO;
	}
	
	

}
