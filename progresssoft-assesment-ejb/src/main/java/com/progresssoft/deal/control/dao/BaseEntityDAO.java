package com.progresssoft.deal.control.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaseEntityDAO<T> implements IEntityDao<T> {

	protected Logger LOG = LoggerFactory.getLogger(this.getClass());

	private EntityManagerFactory entityManagerFactory;
	private EntityManager entityManager;
	private Class<T> entityClass;

	public BaseEntityDAO(EntityManagerFactory entityManagerFactory, Class<T> entityClass) {
		this.entityManagerFactory = entityManagerFactory;
		this.entityClass = entityClass;
	}

	public EntityManager getEntityManager() {
		entityManager = entityManagerFactory.createEntityManager();
		return entityManager;
	}

	public T find(Object primaryKey) throws PersistenceDAOException {
		EntityManager em = getEntityManager();
		T t = null;

		try {
			t = em.find(entityClass, primaryKey);
		} catch (Exception e) {
			throw new PersistenceDAOException("ERROR: Finding " + entityClass.getSimpleName(), e);
		} finally {
			em.close();
		}
		return t;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> findAll() {
		EntityManager em = getEntityManager();
		try {
			String str = "select e from table e";
			String jpql = str.replace("table", entityClass.getName());
			return em.createQuery(jpql).getResultList();
		} finally {
			em.close();
		}
	}

	@Override
	public void save(T entity) throws PersistenceDAOException {
		EntityManager em = getEntityManager();
		try {
			em.getTransaction().begin();
			em.persist(entity);
			em.getTransaction().commit();
		} catch (Exception ex) {
			try {
				if (em.getTransaction().isActive()) {
					em.getTransaction().rollback();
				}
			} catch (Exception e) {
				throw new PersistenceDAOException("ERROR: Persist " + entity.getClass().getName(), e);
			}
			throw new PersistenceDAOException("ERROR: Persist " + entity.getClass().getName(), ex);
		} finally {
			em.close();
		}
	}

	@Override
	public void update(T entity) throws PersistenceDAOException {
		EntityManager em = getEntityManager();
		try {
			em.getTransaction().begin();
			em.merge(entity);
			em.getTransaction().commit();
		} catch (Exception ex) {
			throw new PersistenceDAOException("ERROR: Update " + entity.getClass().getName(), ex);

		} finally {
			em.close();
		}
	}

}
