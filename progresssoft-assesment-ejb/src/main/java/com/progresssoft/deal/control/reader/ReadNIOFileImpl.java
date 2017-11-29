package com.progresssoft.deal.control.reader;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.enterprise.inject.Default;

import com.progresssoft.deal.entity.dto.FileDTO;

@Default
public class ReadNIOFileImpl extends BaseReaderFile {

	private static final long serialVersionUID = 1L;
	
	public List<String> getReadAllLines(FileDTO fileDTO) throws IOException {
		Path path = Paths.get(fileDTO.getPath() + File.separator + fileDTO.getName());
		List<String> readAllLines = Files.readAllLines(path, StandardCharsets.UTF_8);
		readAllLines.remove(0);
		return readAllLines;
	}

}
