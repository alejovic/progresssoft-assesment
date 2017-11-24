package com.progresssoft.deal.entity.dto;

import java.io.Serializable;

public class DealDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	private String id;
	private String isoSource;
	private String isoTarget;
	private String date;
	private String amount;
	private long line;
	private long idFile;

	public DealDTO(String id, String isoSource, String isoTarget, String date, String amount, long line) {
		super();
		this.id = id;
		this.isoSource = isoSource;
		this.isoTarget = isoTarget;
		this.date = date;
		this.amount = amount;
		this.line = line;
	}

	public DealDTO() {
		// TODO Auto-generated constructor stub
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIsoSource() {
		return isoSource;
	}

	public void setIsoSource(String isoSource) {
		this.isoSource = isoSource;
	}

	public String getIsoTarget() {
		return isoTarget;
	}

	public void setIsoTarget(String isoTarget) {
		this.isoTarget = isoTarget;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public long getLine() {
		return line;
	}

	public void setLine(long line) {
		this.line = line;
	}

	public long getIdFile() {
		return idFile;
	}

	public void setIdFile(long idFile) {
		this.idFile = idFile;
	}

	@Override
	public String toString() {
		return "DealDTO [id=" + id + ", isoSource=" + isoSource + ", isoTarget=" + isoTarget + ", date=" + date
				+ ", amount=" + amount + ", line=" + line + "]";
	}

}
