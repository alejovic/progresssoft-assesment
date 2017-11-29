package com.progresssoft.deal.control.reader;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import com.progresssoft.deal.entity.dto.FileDTO;

public interface IReader extends Serializable {

	public List<List<String>> getReadAllLinesBySize(FileDTO fileDTO, int size) throws IOException;

	public List<String> getReadAllLines(FileDTO fileDTO) throws IOException;

}
