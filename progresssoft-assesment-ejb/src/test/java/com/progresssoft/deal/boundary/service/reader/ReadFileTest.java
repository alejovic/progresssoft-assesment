package com.progresssoft.deal.boundary.service.reader;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.junit.Test;

public class ReadFileTest {

	private static final String PATH_CSV_TEST = "/DEAL_SOURCE_TEST.csv";

	@Test
	public void readWithChannel_Test() {
		long startTime = System.nanoTime();

		try {
			URL url = ReadFileTest.class.getResource(PATH_CSV_TEST);
			File file = new File(url.toURI());
			RandomAccessFile aFile = new RandomAccessFile(file, "r");
			FileChannel inChannel = aFile.getChannel();
			MappedByteBuffer buffer = inChannel.map(FileChannel.MapMode.READ_ONLY, 0, inChannel.size());

			buffer.load();

			CharBuffer charBuffer = StandardCharsets.US_ASCII.decode(buffer);
			String read = charBuffer.toString();

			// System.out.println(read);

			buffer.clear(); // do something with the data and clear or compact
							// it.
			inChannel.close();
			aFile.close();

		} catch (IOException | URISyntaxException ioe) {
			ioe.printStackTrace();
		}

		long endTime = System.nanoTime();
		long elapsedTimeInMillis = TimeUnit.MILLISECONDS.convert((endTime - startTime), TimeUnit.NANOSECONDS);
		System.out.println("Total elapsed time: " + elapsedTimeInMillis + " ms");
	}

	@Test
	public void readWithNio_Test() {
		long startTime = System.nanoTime();

		try {
			URL url = ReadFileTest.class.getResource(PATH_CSV_TEST);
			File file = new File(url.toURI());
			Path path = Paths.get(file.getPath());

			List<String> readAllLines = Files.readAllLines(path, StandardCharsets.UTF_8);
			System.out.println(readAllLines.size());

			Object[] lines = (Object[]) readAllLines.toArray();

			for (int i = 0; i < readAllLines.size(); i++) {
				System.out.println(lines[i]);
			}

		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}

		long endTime = System.nanoTime();
		long elapsedTimeInMillis = TimeUnit.MILLISECONDS.convert((endTime - startTime), TimeUnit.NANOSECONDS);
		System.out.println("Total elapsed time: " + elapsedTimeInMillis + " ms");
	}

	@Test
	public void readWithLineIterator_Test() {
		long startTime = System.nanoTime();

		try {
			URL url = ReadFileTest.class.getResource(PATH_CSV_TEST);
			File file = new File(url.toURI());
			LineIterator it = FileUtils.lineIterator(file, StandardCharsets.UTF_8.name());

			try {
				while (it.hasNext()) {
					String line = it.nextLine();
					System.out.println(line);
				}
			} finally {
				LineIterator.closeQuietly(it);
			}

		} catch (IOException | URISyntaxException ioe) {
			ioe.printStackTrace();
		}

		long endTime = System.nanoTime();
		long elapsedTimeInMillis = TimeUnit.MILLISECONDS.convert((endTime - startTime), TimeUnit.NANOSECONDS);
		System.out.println("Total elapsed time: " + elapsedTimeInMillis + " ms");
	}

	@Test
	public void readWithBuffer_Test() {
		long startTime = System.nanoTime();

		try {

			URL url = ReadFileTest.class.getResource(PATH_CSV_TEST);
			File file = new File(url.toURI());
			Path path = Paths.get(file.getPath());

			byte[] data = null;
			data = Files.readAllBytes(path);

			ByteArrayInputStream bais = new ByteArrayInputStream(data);
			InputStreamReader isr = new InputStreamReader(bais);
			BufferedReader br = new BufferedReader(isr);

			String line;
			while ((line = br.readLine()) != null) {
				System.out.println(line);
			}

		} catch (IOException | URISyntaxException ioe) {
			ioe.printStackTrace();
		}

		long endTime = System.nanoTime();
		long elapsedTimeInMillis = TimeUnit.MILLISECONDS.convert((endTime - startTime), TimeUnit.NANOSECONDS);
		System.out.println("Total elapsed time: " + elapsedTimeInMillis + " ms");
	}

}
