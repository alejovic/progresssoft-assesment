package com.progresssoft.deal.control.validator;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import com.progresssoft.deal.entity.model.PsDealData;
import com.progresssoft.deal.entity.model.PsIsoCode;

public abstract class BaseFileDealValidator implements IFileDealValidator {

	private Map<String, Object> ISOCache = new HashMap<>();
	private Map<BigInteger, Object> dealCache = new HashMap<>();
	protected EntityManagerFactory emf;

	public BaseFileDealValidator(EntityManagerFactory emf) {
		this.emf = emf;
	}

	public EntityManager getEntityManager() {
		EntityManager em = emf.createEntityManager();
		return em;
	}

	protected PsIsoCode findISOCodeByCode(String code) throws Exception {

		if (ISOCache.containsKey(code)) {
			return (PsIsoCode) ISOCache.get(code);
		}

		EntityManager em = getEntityManager();
		PsIsoCode psIsoCode = null;
		try {
			Query query = em.createNamedQuery("PsIsoCode.findByCode");
			query.setParameter("code", code);
			psIsoCode = (PsIsoCode) query.getSingleResult();
			ISOCache.put(code, psIsoCode);
		} catch (Exception e) {
			throw new Exception("ERROR: Finding ISOBy Code - " + e.getMessage());
		} finally {
			em.close();
		}
		return psIsoCode;
	}

	protected PsDealData findPsDealByDealId(BigInteger dealId) throws Exception {

		if (dealCache.containsKey(dealId)) {
			return (PsDealData) dealCache.get(dealId);
		}

		EntityManager em = getEntityManager();
		PsDealData psDealData = null;
		try {
			Query query = em.createNamedQuery("PsDealData.findByDealId");
			query.setParameter("dealId", dealId);
			psDealData = (PsDealData) query.getSingleResult();
			dealCache.put(dealId, psDealData);
		} catch (NoResultException e) {
			dealCache.put(dealId, psDealData);
		} catch (Exception e) {
			throw new Exception("ERROR: Finding Deal Id - " + e.getMessage());
		} finally {
			em.close();
		}
		return psDealData;
	}

	public void clearCache() {
		ISOCache = new HashMap<>();
		dealCache = new HashMap<>();
	}

}
