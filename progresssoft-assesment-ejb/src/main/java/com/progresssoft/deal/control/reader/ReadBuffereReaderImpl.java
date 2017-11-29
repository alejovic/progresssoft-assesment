package com.progresssoft.deal.control.reader;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.progresssoft.deal.entity.dto.FileDTO;

public class ReadBuffereReaderImpl extends BaseReaderFile {

	private static final long serialVersionUID = 1L;

	public List<String> getReadAllLines(FileDTO fileDTO) throws IOException {

		Path path = Paths.get(fileDTO.getPath() + File.separator + fileDTO.getName());
		byte[] data = null;
		data = Files.readAllBytes(path);

		ByteArrayInputStream bais = new ByteArrayInputStream(data);
		InputStreamReader isr = new InputStreamReader(bais);
		BufferedReader br = new BufferedReader(isr);

		List<String> readAllLines = new ArrayList<>();
		String line;
		while ((line = br.readLine()) != null) {
			readAllLines.add(line);
		}
		readAllLines.remove(0);
		return readAllLines;
	}

}
