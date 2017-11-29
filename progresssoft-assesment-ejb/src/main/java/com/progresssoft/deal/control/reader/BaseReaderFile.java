package com.progresssoft.deal.control.reader;

import java.io.IOException;
import java.util.List;

import org.apache.commons.collections4.ListUtils;

import com.progresssoft.deal.entity.dto.FileDTO;

public abstract class BaseReaderFile implements IReader {

	private static final long serialVersionUID = 1L;
	
	protected List<List<String>> splitList(List<String> lines, int size) {
		List<List<String>> subLists = ListUtils.partition(lines, size);
		return subLists;
	}

	public List<List<String>> getReadAllLinesBySize(FileDTO fileDTO, int size) throws IOException {
		List<List<String>> listLines = splitList(getReadAllLines(fileDTO), size);
		return listLines;
	}

}
