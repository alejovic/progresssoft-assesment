package com.progresssoft.deal.control.dao;

import java.math.BigInteger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import com.progresssoft.deal.entity.model.PsDealData;
import com.progresssoft.deal.entity.model.PsDealTx;

public class PsDealTxDAO extends BaseEntityDAO<PsDealTx> implements IPsDealTx {

	public PsDealTxDAO(EntityManagerFactory entityManagerFactory, Class<PsDealTx> entityClass) {
		super(entityManagerFactory, entityClass);
	}

	@Override
	public void addDealTx(PsDealData psDealData) throws PersistenceDAOException {

		PsDealTx dealTx = getDealTxByCode(psDealData.getIsoSource());
		if (dealTx != null) {
			dealTx.setCount(dealTx.getCount().add(BigInteger.ONE));
			try {
				update(dealTx);
			} catch (PersistenceDAOException e) {
				LOG.error(e.getMessage());
				throw new PersistenceDAOException(
						"Error Increasing Deal Tx for ISO " + dealTx.getIsoCode() + " - " + e.getMessage(), e);
			}

		} else {
			dealTx = new PsDealTx();
			dealTx.setIsoCode(psDealData.getIsoSource());
			dealTx.setCount(BigInteger.ONE);
			try {
				save(dealTx);
			} catch (PersistenceDAOException e) {
				LOG.error(e.getMessage());
				throw new PersistenceDAOException(
						"Error Initializating Deal Tx for ISO " + dealTx.getIsoCode() + " - " + e.getMessage(), e);
			}
		}
	}

	@Override
	public void updatedDealTx(String code, Long value) throws PersistenceDAOException {

		PsDealTx dealTx = getDealTxByCode(code);
		if (dealTx != null) {
			try {
				BigInteger count= dealTx.getCount();
				count = count.add(BigInteger.valueOf(value));
				dealTx.setCount(count);
				update(dealTx);
			} catch (PersistenceDAOException e) {
				LOG.error(e.getMessage());
				throw new PersistenceDAOException(
						"Error Increasing Deal Tx for ISO " + dealTx.getIsoCode() + " - " + e.getMessage(), e);
			}

		} else {
			try {
			dealTx = new PsDealTx();
			dealTx.setIsoCode(code);
			dealTx.setCount(BigInteger.valueOf(value));
			save(dealTx);
			} catch (PersistenceDAOException e) {
				LOG.error(e.getMessage());
				throw new PersistenceDAOException(
						"Error Init Deal Tx for ISO " + dealTx.getIsoCode() + " - " + e.getMessage(), e);
			}
		}
	}

	@Override
	public PsDealTx getDealTxByCode(String code) throws PersistenceDAOException {
		EntityManager em = getEntityManager();
		PsDealTx psDealTx = null;
		try {
			Query query = em.createNamedQuery("PsDealTx.findByCode");
			query.setParameter("isoCode", code);
			psDealTx = (PsDealTx) query.getSingleResult();
		} catch (NoResultException e) {

		} catch (Exception e) {
			LOG.error(e.getMessage() + "");
			throw new PersistenceDAOException("ERROR: Finding ISO By Code in Deal Tx - " + e.getMessage(), e);
		} finally {
			em.close();
		}
		return psDealTx;
	}

}
