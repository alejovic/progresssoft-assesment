package com.progresssoft.deal.entity.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * The persistent class for the ps_deal_data database table.
 * 
 */
@Entity
@Table(name = "ps_deal_data", schema = "progresssoft")
@NamedQueries({
	@NamedQuery(name="PsDealData.findAll", query="SELECT p FROM PsDealData p"),
	@NamedQuery(name="PsDealData.findByDealId", query="SELECT p FROM PsDealData p WHERE p.dealId = :dealId")
})
public class PsDealData implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	private BigDecimal amount;

	@Temporal(TemporalType.TIMESTAMP)
	private Date date;

	@Column(name="DEAL_ID")
	private BigInteger dealId;

	@Column(name="ISO_SOURCE")
	private String isoSource;

	@Column(name="ISO_TARGET")
	private String isoTarget;

	//bi-directional many-to-one association to PsFile
	@ManyToOne
	@JoinColumn(name="FK_ID_FILE")
	private PsFile psFile;

	public PsDealData() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public BigDecimal getAmount() {
		return this.amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Date getDate() {
		return this.date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public BigInteger getDealId() {
		return this.dealId;
	}

	public void setDealId(BigInteger dealId) {
		this.dealId = dealId;
	}

	public String getIsoSource() {
		return this.isoSource;
	}

	public void setIsoSource(String isoSource) {
		this.isoSource = isoSource;
	}

	public String getIsoTarget() {
		return this.isoTarget;
	}

	public void setIsoTarget(String isoTarget) {
		this.isoTarget = isoTarget;
	}

	public PsFile getPsFile() {
		return this.psFile;
	}

	public void setPsFile(PsFile psFile) {
		this.psFile = psFile;
	}

}