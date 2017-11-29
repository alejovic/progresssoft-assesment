package com.progresssoft.deal.entity.dto;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class DataFileOutDTO implements Serializable, Cloneable {

	private static final long serialVersionUID = 1L;
	private long id;
	private Map<Long, String[]> errorLines = new HashMap<Long, String[]>();
	private Map<String, Long> isoSourceTx = new HashMap<String, Long>();
	private Map<String, Long> isoTargeTx = new HashMap<String, Long>();
	private long countError;
	private long countLine;
	private long timeLoad;

	@Override
	public DataFileOutDTO clone() throws CloneNotSupportedException {
		return (DataFileOutDTO) super.clone();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Map<Long, String[]> getErrorLines() {
		return errorLines;
	}

	public void addErrorLines(String[] message) {
		getErrorLines().put(getCountLine(), message);
	}

	public long getCountError() {
		return countError;
	}

	public void setCountError(long countError) {
		this.countError = countError;
	}

	public void addCountError() {
		this.countError++;
	}

	public long getCountLine() {
		return countLine;
	}

	public void setCountLine(long countLine) {
		this.countLine = countLine;
	}

	public void addCountLine() {
		this.countLine++;
	}

	public long getTimeLoad() {
		return timeLoad;
	}

	public void setTimeLoad(long timeLoad) {
		this.timeLoad = timeLoad;
	}

	public Map<String, Long> getIsoSourceTx() {
		return isoSourceTx;
	}

	public void addIsoSourceTx(String isoCode) {
		if (getIsoSourceTx().containsKey(isoCode)) {
			Long value = (getIsoSourceTx().get(isoCode) != null) ? getIsoSourceTx().get(isoCode) : 0;
			value = value + 1;
			getIsoSourceTx().put(isoCode, value);
		} else {
			getIsoSourceTx().put(isoCode, 1L);
		}
	}

	public Map<String, Long> getIsoTargetTx() {
		return isoTargeTx;
	}

	public void addIsoTargetTx(String isoCode) {
		if (getIsoTargetTx().containsKey(isoCode)) {
			Long value = (getIsoTargetTx().get(isoCode) != null) ? getIsoTargetTx().get(isoCode) : 0;
			value = value + 1;
			getIsoTargetTx().put(isoCode, value);
		} else {
			getIsoTargetTx().put(isoCode, 1L);
		}
	}

	public String getInfoError() {
		final StringBuilder strBuilderError = new StringBuilder();
		Map<Long, String[]> mapErrorLines = getErrorLines();
		Set<Entry<Long, String[]>> eErrorLines = mapErrorLines.entrySet();
		for (Entry<Long, String[]> entry : eErrorLines) {
			strBuilderError.append(entry.getKey() + " | " + entry.getValue()[0] + " | " + entry.getValue()[1]);
			strBuilderError.append("\n");
		}
		return strBuilderError.toString();
	}

	public String getInfoLoad() {
		final StringBuilder strBuilderDetail = new StringBuilder();
		strBuilderDetail.append("Count (Lines) | " + getCountLine());
		strBuilderDetail.append("\n");
		strBuilderDetail.append("Count (Error) | " + getCountError());
		strBuilderDetail.append("\n");
		strBuilderDetail.append("Time (ms)     | " + getTimeLoad());
		strBuilderDetail.append("\n");
		strBuilderDetail.append("ISO Source Tx \n");
		Map<String, Long> mapIsoSource = getIsoSourceTx();
		Set<Entry<String, Long>> eIsoSource = mapIsoSource.entrySet();
		for (Entry<String, Long> entry : eIsoSource) {
			strBuilderDetail.append(entry.getKey() + "|" + entry.getValue());
			strBuilderDetail.append("\n");
		}
		strBuilderDetail.append("\n");
		strBuilderDetail.append("ISO Target Tx \n ");
		Map<String, Long> mapIsoTarget = getIsoTargetTx();
		Set<Entry<String, Long>> eIsoTarget = mapIsoTarget.entrySet();
		for (Entry<String, Long> entry : eIsoTarget) {
			strBuilderDetail.append(entry.getKey() + "|" + entry.getValue());
			strBuilderDetail.append("\n");
		}

		return strBuilderDetail.toString();
	}

	@Override
	public String toString() {
		return "DataFileOutDTO [id=" + id + ", errorLines=" + errorLines + ", isoSourceTx=" + isoSourceTx
				+ ", isoTargeTx=" + isoTargeTx + ", countError=" + countError + ", countLine=" + countLine
				+ ", timeLoad=" + timeLoad + "]";
	}

}
