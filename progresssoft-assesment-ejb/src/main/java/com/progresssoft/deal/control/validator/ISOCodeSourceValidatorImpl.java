package com.progresssoft.deal.control.validator;

import java.text.MessageFormat;

import javax.persistence.EntityManagerFactory;

import com.progresssoft.deal.entity.dto.DealDTO;

public class ISOCodeSourceValidatorImpl extends BaseFileDealValidator {

	public ISOCodeSourceValidatorImpl(EntityManagerFactory emf) {
		super(emf);
	}

	private static String MESSAGE_1 = "ERROR: ISO Code not found [{0}] in line [{1}]";
	private static String MESSAGE_2 = "ERROR: Missing Field ISO Code Source in line [{0}]";

	@Override
	public void validateDeal(DealDTO dealDTO, long line) throws FileDealValidatorException {
		synchronized (this) {

		if ((dealDTO.getIsoSource() != null && dealDTO.getIsoSource().length() > 0)) {
			try {
				findISOCodeByCode(dealDTO.getIsoSource());
			} catch (Exception e) {
				throw new FileDealValidatorException(MessageFormat.format(MESSAGE_1, dealDTO.getIsoSource(), line));
			}
		} else {

			throw new FileDealValidatorException(MessageFormat.format(MESSAGE_2, line));
		}
	}
	}

}
