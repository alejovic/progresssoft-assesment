package com.progresssoft.deal.control.dao;

import java.util.List;

import com.progresssoft.deal.entity.model.PsDealValidator;

public interface IPsDealValidator {

	List<PsDealValidator> getDealValidatorActive() throws PersistenceDAOException;

}