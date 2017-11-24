package com.progresssoft.deal.control.validator.impl;

import java.text.MessageFormat;

import javax.persistence.EntityManagerFactory;

import com.progresssoft.deal.control.validator.BaseFileDealValidator;
import com.progresssoft.deal.control.validator.FileDealValidatorException;
import com.progresssoft.deal.entity.dto.DealDTO;

public class NullValidatorImpl extends BaseFileDealValidator {

	public NullValidatorImpl(EntityManagerFactory emf) {
		super(emf);
	}

	private static String MESSAGE_2 = "ERROR: Missing Field [{0}] in line [{1}]";

	@Override
	public void validateDeal(DealDTO dealDTO, long line) throws FileDealValidatorException {
		synchronized (this) {

			if (dealDTO.getId() == null || (dealDTO.getId() != null && dealDTO.getId().length() == 0)) {
				throw new FileDealValidatorException(MessageFormat.format(MESSAGE_2, "Deal Id", line));
			}

			if (dealDTO.getIsoSource() == null
					|| (dealDTO.getIsoSource() != null && dealDTO.getIsoSource().length() == 0)) {
				throw new FileDealValidatorException(MessageFormat.format(MESSAGE_2, "ISO Source", line));
			}

			if (dealDTO.getIsoTarget() == null
					|| (dealDTO.getIsoTarget() != null && dealDTO.getIsoTarget().length() == 0)) {
				throw new FileDealValidatorException(MessageFormat.format(MESSAGE_2, "ISO Target", line));
			}

			if (dealDTO.getDate() == null) {
				throw new FileDealValidatorException(MessageFormat.format(MESSAGE_2, "TimeStamp", line));
			}

			if (dealDTO.getAmount() == null || (dealDTO.getAmount() != null && dealDTO.getAmount().length() == 0)) {
				throw new FileDealValidatorException(MessageFormat.format(MESSAGE_2, "Amount", line));
			}
		}
	}

}
