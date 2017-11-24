package com.progresssoft.deal.common;

import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;

public class EntityManagerProducer {
	
	private static final String PROGRESSOFT_ASSESMENT_TEST = "progressoft-assesment-test";

	@Produces
	public EntityManager createEM() {
	   return Persistence.createEntityManagerFactory(PROGRESSOFT_ASSESMENT_TEST).createEntityManager();
	}

	public void closeEM(@Disposes EntityManager em) {
	   em.close();
	}

}
