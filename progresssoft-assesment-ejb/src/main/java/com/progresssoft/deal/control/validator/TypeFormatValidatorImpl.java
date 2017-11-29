package com.progresssoft.deal.control.validator.impl;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Pattern;

import javax.persistence.EntityManagerFactory;

import com.progresssoft.deal.control.validator.BaseFileDealValidator;
import com.progresssoft.deal.control.validator.FileDealValidatorException;
import com.progresssoft.deal.entity.dto.DealDTO;

public class TypeFormatValidatorImpl extends BaseFileDealValidator {

	public TypeFormatValidatorImpl(EntityManagerFactory emf) {
		super(emf);
	}

	private static String MESSAGE_1 = "ERROR: Field not valid. expect [{0}] instead [{1}] in line [{2}]";
	private static final Pattern NUMBER_PATTERN = Pattern.compile("-?\\d+");
	private static final Pattern ISO_PATTERN = Pattern.compile("^[A-Z]{3}$");
	private static final SimpleDateFormat SDF_FORMAT = new SimpleDateFormat("YYYY/MM/dd HH:mm:ss");
	private static final Pattern DECIMAL_PATTERN = Pattern.compile("\\d+(\\.\\d{1,2})?");

	@Override
	public void validateDeal(DealDTO dealDTO, long line) throws FileDealValidatorException {
		synchronized (this) {
			
			if ((dealDTO.getId() != null && dealDTO.getId() != "")) {
				if (!isNumber(dealDTO.getId())) {
					throw new FileDealValidatorException(
							MessageFormat.format(MESSAGE_1, "Numeric", dealDTO.getId(), line));
				}
			}

			if ((dealDTO.getIsoSource() != null && dealDTO.getIsoSource() != "")) {
				if (!isISOCode(dealDTO.getIsoSource())) {
					throw new FileDealValidatorException(
							MessageFormat.format(MESSAGE_1, "ISO Code", dealDTO.getIsoSource(), line));
				}
			}

			if ((dealDTO.getIsoTarget() != null && dealDTO.getIsoTarget() != "")) {
				if (!isISOCode(dealDTO.getIsoTarget())) {
					throw new FileDealValidatorException(
							MessageFormat.format(MESSAGE_1, "ISO Code", dealDTO.getIsoTarget(), line));
				}
			}

			if ((dealDTO.getDate() != null && dealDTO.getDate() != "")) {
				if (!isDateFormat(dealDTO.getDate())) {
					throw new FileDealValidatorException(MessageFormat.format(MESSAGE_1,
							"Date Format (YYYY/MM/dd HH:mm:ss)", dealDTO.getDate(), line));
				}
			}

			if ((dealDTO.getAmount() != null && dealDTO.getAmount() != "")) {
				if (!isDecimal(dealDTO.getAmount())) {
					throw new FileDealValidatorException(
							MessageFormat.format(MESSAGE_1, "Decimal(15,2)", dealDTO.getAmount(), line));
				}
			}
		}
	}

	public static boolean isNumber(String string) {
		return string != null && NUMBER_PATTERN.matcher(string).matches();
	}

	public static boolean isISOCode(String string) {
		return string != null && ISO_PATTERN.matcher(string).matches();
	}

	public static boolean isDateFormat(String string) {
		try {
			SDF_FORMAT.parse(string);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public static boolean isDecimal(String string) {
		return string != null && DECIMAL_PATTERN.matcher(string).matches();
	}

}
