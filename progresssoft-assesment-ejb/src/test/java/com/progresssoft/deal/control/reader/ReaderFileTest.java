package com.progresssoft.deal.control.reader;

import static org.junit.Assert.assertEquals;
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
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.progresssoft.deal.entity.dto.FileDTO;

public class ReaderFileTest {

	private static final String PATH_CSV_TEST = "/DEAL_SOURCE_TEST.csv";

	private FileDTO fileDTO;
	private IReader reader;

	protected Logger LOG = LoggerFactory.getLogger(this.getClass());

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		URL url = ReaderFileTest.class.getResource(PATH_CSV_TEST);

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

	@Test
	public void readNIOFile_Test() {
		long startTime = System.nanoTime();

		List<String> lines = null;
		reader = new ReadNIOFileImpl();
		try {
			lines = reader.getReadAllLines(fileDTO);
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("ReadNIOFile -> " + lines.size());

		long endTime = System.nanoTime();
		long elapsedTimeInMillis = TimeUnit.MILLISECONDS.convert((endTime - startTime), TimeUnit.NANOSECONDS);
		System.out.println("Total elapsed time: " + elapsedTimeInMillis + " ms");
		
		int expected = 100290;
		assertEquals(lines.size(), expected);
	}

	@Test
	public void readBuffereReader_Test() {
		long startTime = System.nanoTime();

		List<String> lines = null;
		reader = new ReadBuffereReaderImpl();
		try {
			lines = reader.getReadAllLines(fileDTO);
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("ReadBuffereReader -> " + lines.size());

		long endTime = System.nanoTime();
		long elapsedTimeInMillis = TimeUnit.MILLISECONDS.convert((endTime - startTime), TimeUnit.NANOSECONDS);
		System.out.println("Total elapsed time: " + elapsedTimeInMillis + " ms");
		
		int expected = 100290;
		assertEquals(lines.size(), expected);
	}

	@Test
	public void readLineIterator() {
		long startTime = System.nanoTime();

		List<String> lines = null;
		reader = new ReadLineIteratorImpl();
		try {
			lines = reader.getReadAllLines(fileDTO);
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("ReadLineIterator -> " + lines.size());

		long endTime = System.nanoTime();
		long elapsedTimeInMillis = TimeUnit.MILLISECONDS.convert((endTime - startTime), TimeUnit.NANOSECONDS);
		System.out.println("Total elapsed time: " + elapsedTimeInMillis + " ms");
		
		int expected = 100290;
		assertEquals(lines.size(), expected);
	}

}
