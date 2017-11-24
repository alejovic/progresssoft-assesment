package com.progresssoft.deal.entity.model;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigInteger;


/**
 * The persistent class for the ps_deal_tx database table.
 * 
 */
@Entity
@Table(name="ps_deal_tx", schema = "progresssoft")
@NamedQueries({
	@NamedQuery(name="PsDealTx.findAll", query="SELECT p FROM PsDealTx p"),
	@NamedQuery(name="PsDealTx.findByCode", query="SELECT p FROM PsDealTx p WHERE p.isoCode = :isoCode")
})
public class PsDealTx implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private String id;

	private BigInteger count;

	@Column(name="ISO_CODE")
	private String isoCode;

	public PsDealTx() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public BigInteger getCount() {
		return this.count;
	}

	public void setCount(BigInteger count) {
		this.count = count;
	}

	public String getIsoCode() {
		return this.isoCode;
	}

	public void setIsoCode(String isoCode) {
		this.isoCode = isoCode;
	}

}