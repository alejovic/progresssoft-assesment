package com.progresssoft.deal.boundary.facade;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.progresssoft.deal.boundary.service.DealService;
import com.progresssoft.deal.boundary.service.DealServiceException;
import com.progresssoft.deal.control.validator.IFileDealValidator;
import com.progresssoft.deal.entity.dto.DataFileInDTO;
import com.progresssoft.deal.entity.dto.DataFileOutDTO;

/**
 * Session Bean implementation class DealFacadeBean
 */
@Stateless(name = "DealFacadeBean", mappedName = "ProgressSoft-EJB-DealFacadeBean")
// @LocalBean
public class DealFacadeBean implements DealFacade {

	@EJB(beanName = "DealServiceBean")
	DealService dealService;

	private Logger LOG = LoggerFactory.getLogger(this.getClass());

	/**
	 * Default constructor.
	 */
	public DealFacadeBean() {

	}

	public void loadDealValidator(DataFileInDTO dataFileDTO) throws BusinessException {
		List<IFileDealValidator> lstValidator;
		try {
			lstValidator = dealService.getDealValidatorsAvailable();
		} catch (DealServiceException e) {
			LOG.error(e.getMessage());
			throw new BusinessException("BusinessError[CODE100] - " + e.getMessage());
		}
		dataFileDTO.setValidators(lstValidator);
	}

	public DataFileOutDTO processDataFileDeal(DataFileInDTO dataFileInDTO) throws BusinessException {

		DataFileOutDTO dataFileOutDTO = null;
		try {
			dataFileOutDTO = dealService.readFile(dataFileInDTO);
		} catch (DealServiceException e) {
			LOG.error(e.getMessage());
			throw new BusinessException("BusinessError[CODE101] - " + e.getMessage());
		}

		try {
			dealService.processErrorDeal(dataFileOutDTO);
		} catch (DealServiceException e) {
			LOG.error(e.getMessage());
			throw new BusinessException("BusinessError[CODE102] - " + e.getMessage());
		}

		try {
			dealService.processDealTx(dataFileOutDTO);
		} catch (DealServiceException e) {
			LOG.error(e.getMessage());
			throw new BusinessException("BusinessError[CODE103] - " + e.getMessage());
		}

		return dataFileOutDTO;

	}

}
