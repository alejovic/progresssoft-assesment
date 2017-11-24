package com.progresssoft.deal.control.dao;

import java.util.List;

public interface IEntityDao<T> {

    T find(Object primaryKe) throws PersistenceDAOException;

    List<T> findAll() throws PersistenceDAOException;

    void save(T entity) throws PersistenceDAOException;

	void update(T entity) throws PersistenceDAOException;




}