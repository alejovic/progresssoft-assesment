package com.progresssoft.deal.resource;

import java.util.ArrayList;
import java.util.List;

import com.progresssoft.deal.entity.dto.DealDTO;

public class CSVGeneratorImpl {

	private CSVGeneratorService csvGeneratorService;

	public CSVGeneratorImpl(CSVGeneratorService csvGeneratorService) {
		super();
		this.csvGeneratorService = csvGeneratorService;
	}

	public List<DealDTO> loadDefaultList() {
		List<DealDTO> lstResult = new ArrayList<DealDTO>();
		List<DealDTO> dealList = csvGeneratorService.loadData();
		for (DealDTO csvDealDTO : dealList) {
			if(!csvDealDTO.getIsoSource().equalsIgnoreCase(csvDealDTO.getIsoTarget())){
				lstResult.add(csvDealDTO);
			}
		}
		return lstResult;
	}

}
