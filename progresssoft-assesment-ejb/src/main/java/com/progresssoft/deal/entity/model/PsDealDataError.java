package com.progresssoft.deal.entity.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.math.BigInteger;


/**
 * The persistent class for the ps_deal_data_error database table.
 * 
 */
@Entity
@Table(name="ps_deal_data_error", schema = "progresssoft")
@NamedQuery(name="PsDealDataError.findAll", query="SELECT p FROM PsDealDataError p")
public class PsDealDataError implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private String id;

	@Temporal(TemporalType.TIMESTAMP)
	private Date date;

	@Column(name="DEAL_ERROR")
	private String dealError;

	private BigInteger line;

	@Column(name="MESSSAGE_ERROR")
	private String messsageError;

	//bi-directional many-to-one association to PsFile
	@ManyToOne
	@JoinColumn(name="FK_ID_FILE")
	private PsFile psFile;

	public PsDealDataError() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getDate() {
		return this.date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getDealError() {
		return this.dealError;
	}

	public void setDealError(String dealError) {
		this.dealError = dealError;
	}

	public BigInteger getLine() {
		return this.line;
	}

	public void setLine(BigInteger line) {
		this.line = line;
	}

	public String getMesssageError() {
		return this.messsageError;
	}

	public void setMesssageError(String messsageError) {
		this.messsageError = messsageError;
	}

	public PsFile getPsFile() {
		return this.psFile;
	}

	public void setPsFile(PsFile psFile) {
		this.psFile = psFile;
	}

}