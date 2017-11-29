package com.progresssoft.deal.control.persistence;

import javax.naming.OperationNotSupportedException;

public class DealPersistenceFactory {

	public static BaseDealPersistence getInstance(DealPersistenceEnum persistenceEnum)
			throws OperationNotSupportedException {
		switch (persistenceEnum) {
		case HIBERNATE:
			return new DealHibernateImpl();
		case MULTIROW:
			return new DealMultiRowImpl();
		case JPA:
			return new DealJPAImpl();
		case NATIVEQUERY:
			return new DealNativeQueryImpl();
		}
		throw new OperationNotSupportedException("Implementation has not build.");
	}

}
