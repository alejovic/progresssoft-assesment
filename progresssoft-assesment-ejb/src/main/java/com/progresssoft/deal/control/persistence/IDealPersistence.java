package com.progresssoft.deal.control.persistence;

import java.io.Serializable;

import com.progresssoft.deal.entity.dto.DataFileOutDTO;

public interface IDealPersistence extends Serializable {

	public abstract DataFileOutDTO execute();

}
