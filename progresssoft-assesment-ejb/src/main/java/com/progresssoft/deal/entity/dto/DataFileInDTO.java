package com.progresssoft.deal.entity.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.progresssoft.deal.control.validator.IFileDealValidator;

public class DataFileInDTO implements Serializable, Cloneable {

	private static final long serialVersionUID = 1L;
	private long id;
	private Date date;
	private List<IFileDealValidator> validators = new ArrayList<IFileDealValidator>();
	private FileDTO fileDTO;

	@Override
	public DataFileInDTO clone() throws CloneNotSupportedException {
		return (DataFileInDTO) super.clone();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public List<IFileDealValidator> getValidators() {
		return validators;
	}

	public void setValidators(List<IFileDealValidator> validators) {
		this.validators = validators;
	}

	public FileDTO getFileDTO() {
		return fileDTO;
	}

	public void setFileDTO(FileDTO fileDTO) {
		this.fileDTO = fileDTO;
	}

}
