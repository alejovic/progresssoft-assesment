package com.progresssoft.deal.control.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import com.progresssoft.deal.entity.model.PsDealValidator;

public class PsDealValidatorDAO extends BaseEntityDAO<PsDealValidator> implements IPsDealValidator {

	private static final String TRUE = "T";

	public PsDealValidatorDAO(EntityManagerFactory entityManagerFactory, Class<PsDealValidator> entityClass) {
		super(entityManagerFactory, entityClass);
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<PsDealValidator> getDealValidatorActive() throws PersistenceDAOException {
		EntityManager em = getEntityManager();
		List<PsDealValidator> result = null;
		try {
			Query query = em.createNamedQuery("PsDealValidator.findByActive");
			query.setParameter("active", TRUE);
			result = query.getResultList();
		} catch (NoResultException e) {

		} catch (Exception e) {
			LOG.error(e.getMessage());
			throw new PersistenceDAOException("ERROR: Finding Active Validator in BD - " + e.getMessage(), e);
		} finally {
			em.close();
		}
		return result;
	}

}
