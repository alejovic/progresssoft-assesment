package com.progresssoft.deal.control.validator;

import java.text.MessageFormat;

import javax.persistence.EntityManagerFactory;

import com.progresssoft.deal.entity.dto.DealDTO;

public class ISOCodeTargetValidatorImpl extends BaseFileDealValidator {

	public ISOCodeTargetValidatorImpl(EntityManagerFactory emf) {
		super(emf);
	}

	private static String MESSAGE_1 = "ERROR: ISO Code not found [{0}] in line [{1}]";
	private static String MESSAGE_2 = "ERROR: Missing Field ISO Code Target in line [{0}]";

	@Override
	public void validateDeal(DealDTO dealDTO, long line) throws FileDealValidatorException {
		synchronized (this) {

			if ((dealDTO.getIsoTarget() != null && dealDTO.getIsoTarget().length() > 0)) {
				try {
					findISOCodeByCode(dealDTO.getIsoTarget());
				} catch (Exception e) {
					throw new FileDealValidatorException(MessageFormat.format(MESSAGE_1, dealDTO.getIsoTarget(), line));
				}
			} else {

				throw new FileDealValidatorException(MessageFormat.format(MESSAGE_2, line));
			}
		}
	}

}
