package com.progresssoft.deal.control.reader;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

import com.progresssoft.deal.entity.dto.FileDTO;

public class ReadLineIteratorImpl extends BaseReaderFile {

	private static final long serialVersionUID = 1L;
	
	public List<String> getReadAllLines(FileDTO fileDTO) throws IOException {

		File file = new File(fileDTO.getPath() + File.separator + fileDTO.getName());
		LineIterator it = FileUtils.lineIterator(file, StandardCharsets.UTF_8.name());

		List<String> readAllLines = new ArrayList<>();

		try {
			while (it.hasNext()) {
				readAllLines.add(it.nextLine());
			}
		} finally {
			LineIterator.closeQuietly(it);
		}
		readAllLines.remove(0);
		return readAllLines;
	}

}
