package com.progresssoft.deal.view;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.servlet.http.Part;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.progresssoft.deal.boundary.facade.BusinessException;
import com.progresssoft.deal.boundary.facade.DealFacade;
import com.progresssoft.deal.entity.dto.DataFileInDTO;
import com.progresssoft.deal.entity.dto.DataFileOutDTO;
import com.progresssoft.deal.entity.dto.FileDTO;

@ManagedBean(name="dealUploadView")
@RequestScoped
public class DealUploadView implements Serializable {

	private Logger LOG = LoggerFactory.getLogger(this.getClass());
	
	private static final long serialVersionUID = 1L;
	private Part uploadedFile;
	private String fileContent;
	private String fileErrorContent;
	private String fileDetailContent;
	
	private DataFileInDTO dataFileDTO = new DataFileInDTO();

	@EJB
	private DealFacade dealFacade;

	@PostConstruct
	public void init() {
		try {
			dealFacade.loadDealValidator(dataFileDTO);
		} catch (BusinessException e) {
			List<FacesMessage> msgs = new ArrayList<FacesMessage>();
			msgs.add(new FacesMessage("ERROR: File too big", e.getMessage()));
			throw new ValidatorException(msgs);
		}
	}

	public void validateFile(FacesContext ctx, UIComponent comp, Object value) {
		List<FacesMessage> msgs = new ArrayList<FacesMessage>();
		Part file = (Part) value;
		int byteSize = 1024 * 10000;
		if (file.getSize() > byteSize) {
			msgs.add(new FacesMessage("ERROR: File too big"));
		}
		if (!"application/vnd.ms-excel".equals(file.getContentType())) {
			msgs.add(new FacesMessage("ERROR: not a CSV file"));
		}
		if (!msgs.isEmpty()) {
			throw new ValidatorException(msgs);
		}
	}

	@SuppressWarnings("resource")
	public void uploadFile() {
		try {

			InputStream is = uploadedFile.getInputStream();
			byte[] bytes = IOUtils.toByteArray(is);

			FileDTO fileDTO = new FileDTO();
			fileDTO.setName(uploadedFile.getSubmittedFileName());
			fileDTO.setContentType(uploadedFile.getContentType());
			fileDTO.setSize(uploadedFile.getSize());
			fileDTO.setFile(bytes);

			dataFileDTO.setId(1);
			dataFileDTO.setFileDTO(fileDTO);
			
			DataFileOutDTO dataFileOutDTO = dealFacade.processDataFileDeal(dataFileDTO);
			fileDetailContent = dataFileOutDTO.getInfoLoad();
			fileErrorContent = dataFileOutDTO.getInfoError();
					
			fileContent = new Scanner(uploadedFile.getInputStream()).useDelimiter("\\A").next();
		} catch (IOException e) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "error uploading file", null);
			FacesContext.getCurrentInstance().addMessage(null, msg);
		} catch (BusinessException e) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null);
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
	}

	public Part getUploadedFile() {
		return uploadedFile;
	}

	public String getFileContent() {
		return fileContent;
	}

	public void setFileContent(String fileContent) {
		this.fileContent = fileContent;
	}

	public String getFileErrorContent() {
		return fileErrorContent;
	}

	public void setFileErrorContent(String fileErrorContent) {
		this.fileErrorContent = fileErrorContent;
	}

	public String getFileDetailContent() {
		return fileDetailContent;
	}

	public void setFileDetailContent(String fileDetailContent) {
		this.fileDetailContent = fileDetailContent;
	}

	public DataFileInDTO getDataFileDTO() {
		return dataFileDTO;
	}

	public void setDataFileDTO(DataFileInDTO dataFileDTO) {
		this.dataFileDTO = dataFileDTO;
	}

	public DealFacade getDealFacade() {
		return dealFacade;
	}

	public void setDealFacade(DealFacade dealFacade) {
		this.dealFacade = dealFacade;
	}

	public void setUploadedFile(Part uploadedFile) {
		this.uploadedFile = uploadedFile;
	}

	

}
