package com.progresssoft.deal.control.validator.impl;

import java.math.BigInteger;
import java.text.MessageFormat;

import javax.persistence.EntityManagerFactory;

import com.progresssoft.deal.control.validator.BaseFileDealValidator;
import com.progresssoft.deal.control.validator.FileDealValidatorException;
import com.progresssoft.deal.entity.dto.DealDTO;
import com.progresssoft.deal.entity.model.PsDealData;

public class UniqueDealValidatorImpl extends BaseFileDealValidator {

	public UniqueDealValidatorImpl(EntityManagerFactory emf) {
		super(emf);
	}

	private static String MESSAGE_1 = "ERROR: Deal ID Exist [{0}] in line [{1}]";
	private static String MESSAGE_2 = "ERROR: Missing Field Deal Id in line [{0}]";

	@Override
	public void validateDeal(DealDTO dealDTO, long line) throws FileDealValidatorException {
		synchronized (this) {

			if ((dealDTO.getId() != null && dealDTO.getId().length() > 0)) {
				PsDealData psDealData = null;
				try {
					psDealData = findPsDealByDealId(new BigInteger(dealDTO.getId()));
				} catch (Exception e) {

				}

				if (psDealData != null) {
					throw new FileDealValidatorException(MessageFormat.format(MESSAGE_1, dealDTO.getId(), line));
				}

			} else {

				throw new FileDealValidatorException(MessageFormat.format(MESSAGE_2, line));
			}
		}

	}

}
