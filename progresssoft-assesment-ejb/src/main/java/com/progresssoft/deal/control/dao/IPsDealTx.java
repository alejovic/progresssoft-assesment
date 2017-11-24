package com.progresssoft.deal.control.dao;

import com.progresssoft.deal.entity.model.PsDealData;
import com.progresssoft.deal.entity.model.PsDealTx;

public interface IPsDealTx {

	void addDealTx(PsDealData psDealData) throws PersistenceDAOException;

	void updatedDealTx(String code, Long value) throws PersistenceDAOException;
	
	PsDealTx getDealTxByCode(String code) throws PersistenceDAOException;

}