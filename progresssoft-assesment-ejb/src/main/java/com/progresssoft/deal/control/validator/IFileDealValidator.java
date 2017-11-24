package com.progresssoft.deal.control.validator;

import com.progresssoft.deal.entity.dto.DealDTO;

public interface IFileDealValidator {

	 public void validateDeal(DealDTO dealDTO, long line) throws FileDealValidatorException;

}
