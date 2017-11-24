package com.progresssoft.deal.entity.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the ps_deal_validator database table.
 * 
 */
@Entity
@Table(name="ps_deal_validator", schema = "progresssoft")
@NamedQueries({
	@NamedQuery(name="PsDealValidator.findAll", query="SELECT p FROM PsDealValidator p"),
	@NamedQuery(name="PsDealValidator.findByActive", query="SELECT p FROM PsDealValidator p WHERE p.active = :active")
})
public class PsDealValidator implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	private String active;

	@Column(name="CLASS")
	private String class_;

	public PsDealValidator() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getActive() {
		return this.active;
	}

	public void setActive(String active) {
		this.active = active;
	}

	public String getClass_() {
		return this.class_;
	}

	public void setClass_(String class_) {
		this.class_ = class_;
	}

}