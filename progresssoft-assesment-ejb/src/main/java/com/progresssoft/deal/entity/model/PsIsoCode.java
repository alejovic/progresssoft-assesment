package com.progresssoft.deal.entity.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the ps_iso_code database table.
 * 
 */
@Entity
@Table(name="ps_iso_code", schema = "progresssoft")
@NamedQueries({
	@NamedQuery(name="PsIsoCode.findAll", query="SELECT p FROM PsIsoCode p"),
	@NamedQuery(name="PsIsoCode.findByCode", query="SELECT p FROM PsIsoCode p WHERE p.code = :code")
})
public class PsIsoCode implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	//@GeneratedValue(strategy = GenerationType.AUTO)
	private String id;

	private String code;

	private String currency;

	private String location;

	public PsIsoCode() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCurrency() {
		return this.currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getLocation() {
		return this.location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

}