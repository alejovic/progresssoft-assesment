package com.progresssoft.deal.entity.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.math.BigInteger;
import java.util.List;


/**
 * The persistent class for the ps_file database table.
 * 
 */
@Entity
@Table(name="ps_file", schema = "progresssoft")
@NamedQuery(name="PsFile.findAll", query="SELECT p FROM PsFile p")
public class PsFile implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Temporal(TemporalType.TIMESTAMP)
	private Date date;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="END_LOAD")
	private Date endLoad;

	private BigInteger fail;

	@Lob
	private byte[] file;

	private String name;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="START_LOAD")
	private Date startLoad;

	private BigInteger total;

	//bi-directional many-to-one association to PsDealData
	@OneToMany(mappedBy="psFile")
	private List<PsDealData> psDealData;

	//bi-directional many-to-one association to PsDealDataError
	@OneToMany(mappedBy="psFile")
	private List<PsDealDataError> psDealDataErrors;

	public PsFile() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Date getDate() {
		return this.date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Date getEndLoad() {
		return this.endLoad;
	}

	public void setEndLoad(Date endLoad) {
		this.endLoad = endLoad;
	}

	public BigInteger getFail() {
		return this.fail;
	}

	public void setFail(BigInteger fail) {
		this.fail = fail;
	}

	public byte[] getFile() {
		return this.file;
	}

	public void setFile(byte[] file) {
		this.file = file;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getStartLoad() {
		return this.startLoad;
	}

	public void setStartLoad(Date startLoad) {
		this.startLoad = startLoad;
	}

	public BigInteger getTotal() {
		return this.total;
	}

	public void setTotal(BigInteger total) {
		this.total = total;
	}

	public List<PsDealData> getPsDealData() {
		return this.psDealData;
	}

	public void setPsDealData(List<PsDealData> psDealData) {
		this.psDealData = psDealData;
	}

	public PsDealData addPsDealData(PsDealData psDealData) {
		getPsDealData().add(psDealData);
		psDealData.setPsFile(this);

		return psDealData;
	}

	public PsDealData removePsDealData(PsDealData psDealData) {
		getPsDealData().remove(psDealData);
		psDealData.setPsFile(null);

		return psDealData;
	}

	public List<PsDealDataError> getPsDealDataErrors() {
		return this.psDealDataErrors;
	}

	public void setPsDealDataErrors(List<PsDealDataError> psDealDataErrors) {
		this.psDealDataErrors = psDealDataErrors;
	}

	public PsDealDataError addPsDealDataError(PsDealDataError psDealDataError) {
		getPsDealDataErrors().add(psDealDataError);
		psDealDataError.setPsFile(this);

		return psDealDataError;
	}

	public PsDealDataError removePsDealDataError(PsDealDataError psDealDataError) {
		getPsDealDataErrors().remove(psDealDataError);
		psDealDataError.setPsFile(null);

		return psDealDataError;
	}

}