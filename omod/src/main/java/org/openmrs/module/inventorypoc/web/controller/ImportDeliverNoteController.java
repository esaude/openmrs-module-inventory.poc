/**
 *
 */
package org.openmrs.module.inventorypoc.web.controller;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.openmrs.Drug;
import org.openmrs.api.context.Context;
import org.openmrs.module.inventorypoc.delivernote.model.DeliverNote;
import org.openmrs.module.inventorypoc.delivernote.model.DeliverNoteItem;
import org.openmrs.module.inventorypoc.delivernote.service.DeliverNoteService;
import org.openmrs.module.inventorypoc.drugpackage.model.DrugPackage;
import org.openmrs.module.inventorypoc.drugpackage.service.DrugPackageService;
import org.openmrs.module.inventorypoc.web.bean.DeliverNoteItemRow;
import org.openmrs.module.inventorypoc.web.bean.DeliverNoteRow;
import org.openmrs.module.inventorypoc.web.bean.UploadFile;
import org.openmrs.module.inventorypoc.web.transformer.XlsToPojoObjectTransformer;
import org.openmrs.module.inventorypoc.web.utils.WebUtils;
import org.openmrs.module.inventorypoc.web.validator.DeliverNoteRowValidator;
import org.openmrs.module.inventorypoc.web.validator.UploadFileValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;

@Controller(ImportDeliverNoteController.MODULE_ID + ".ImportDeliverNote")
@SessionAttributes({ ImportDeliverNoteController.DELIVER_NOTE })
public class ImportDeliverNoteController {
	
	public static final String MODULE_ID = "inventorypoc";
	
	public static final String MODULE_PATH = "/module/" + ImportDeliverNoteController.MODULE_ID;
	
	public static final String CONTROLLER_PATH = ImportDeliverNoteController.MODULE_PATH + "/deliverNote";
	
	public static final String UPLOAD_FORM_PATH = ImportDeliverNoteController.CONTROLLER_PATH + "/upload";
	
	public static final String IMPORT_FORM_PATH = ImportDeliverNoteController.CONTROLLER_PATH + "/import";
	
	public static final String VALIDATE_FORM_PATH = ImportDeliverNoteController.CONTROLLER_PATH + "/validate";
	
	public static final String DELIVER_NOTE = "deliverNote";
	
	public static final String UPLOAD_FILE = "uploadFile";
	
	@Autowired
	private XlsToPojoObjectTransformer xlsToPojoObjectTransformer;
	
	@Autowired
	private UploadFileValidator uploadFileValidator;
	
	@Autowired
	private DeliverNoteRowValidator deliverNoteRowValidator;
	
	@RequestMapping(value = ImportDeliverNoteController.UPLOAD_FORM_PATH, method = RequestMethod.GET)
	public void upload(final SessionStatus status) {
		status.setComplete();
	}
	
	@RequestMapping(value = ImportDeliverNoteController.UPLOAD_FORM_PATH, method = RequestMethod.POST)
	public ModelAndView uploadPOST(final UploadFile uploadFile, final Errors errors, final Model model,
	        final HttpSession session) throws IOException {
		
		this.uploadFileValidator.validate(uploadFile, errors);
		
		if (errors.hasErrors()) {
			return new ModelAndView();
		}
		
		final File file = new File(uploadFile.getFile().getName());
		uploadFile.getFile().transferTo(file);
		
		final DeliverNoteRow deliverNote = this.xlsToPojoObjectTransformer.toDeliverNote(file);
		model.addAttribute(ImportDeliverNoteController.DELIVER_NOTE, deliverNote);
		
		return new ModelAndView(WebUtils.redirect(ImportDeliverNoteController.VALIDATE_FORM_PATH));
	}
	
	@RequestMapping(value = ImportDeliverNoteController.VALIDATE_FORM_PATH, method = RequestMethod.GET)
	public ModelAndView validate(
	        @ModelAttribute(ImportDeliverNoteController.DELIVER_NOTE) final DeliverNoteRow deliverNoteRow,
	        final Errors errors) {
		
		this.deliverNoteRowValidator.validate(deliverNoteRow, errors);
		
		final ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject(ImportDeliverNoteController.DELIVER_NOTE, deliverNoteRow);
		modelAndView.addObject("hasErrors", errors.hasErrors());
		
		return modelAndView;
	}
	
	@RequestMapping(value = ImportDeliverNoteController.VALIDATE_FORM_PATH, method = RequestMethod.POST)
	public ModelAndView validatePost(
	        @ModelAttribute(ImportDeliverNoteController.DELIVER_NOTE) final DeliverNoteRow deliverNoteRow,
	        final Errors errors, final Model model) {
		
		this.deliverNoteRowValidator.validate(deliverNoteRow, errors);
		
		if (errors.hasErrors()) {
			return new ModelAndView(ImportDeliverNoteController.VALIDATE_FORM_PATH);
		}
		
		this.setDrugNameSystemDesignation(deliverNoteRow);
		model.addAttribute(ImportDeliverNoteController.DELIVER_NOTE, deliverNoteRow);
		
		return new ModelAndView(WebUtils.redirect(ImportDeliverNoteController.IMPORT_FORM_PATH));
	}
	
	@RequestMapping(value = ImportDeliverNoteController.IMPORT_FORM_PATH, method = RequestMethod.GET)
	public ModelAndView importGet(
	        @ModelAttribute(ImportDeliverNoteController.DELIVER_NOTE) final DeliverNoteRow deliverNote) {
		
		return new ModelAndView();
	}
	
	@RequestMapping(value = ImportDeliverNoteController.IMPORT_FORM_PATH, method = RequestMethod.POST)
	public ModelAndView importPost(
	        @ModelAttribute(ImportDeliverNoteController.DELIVER_NOTE) final DeliverNoteRow deliverNote)
	        throws ParseException {
		
		final DeliverNote deliverNoteToImport = this.generateDeliverNote(deliverNote);
		final DeliverNoteService deliverNoteService = Context.getService(DeliverNoteService.class);
		deliverNoteService.importDeliverNote(deliverNoteToImport);
		
		final ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("openmrs_msg", "inventorypoc.imported");
		
		return modelAndView;
	}
	
	private DeliverNote generateDeliverNote(final DeliverNoteRow deliverNoteRow) throws ParseException {
		
		final DeliverNote deliverNoteToImport = new DeliverNote();
		
		final SimpleDateFormat sfd = new SimpleDateFormat("dd/MM/yyyy");
		final Date deliverDate = sfd.parse(deliverNoteRow.getDeliveryDate());
		
		deliverNoteToImport.setDeliveryDate(deliverDate);
		deliverNoteToImport.setSimamNumber(deliverNoteRow.getSimamNumber());
		deliverNoteToImport.setLocation(Context.getLocationService().getDefaultLocation());
		
		for (final DeliverNoteItemRow itemRow : deliverNoteRow.getItems()) {
			
			final DeliverNoteItem noteItem = new DeliverNoteItem();
			noteItem.setDeliverNote(deliverNoteToImport);
			
			noteItem.setRequestedQuantity(Double.valueOf(itemRow.getRequestedQuantity()));
			noteItem.setAuthorizedQuantity(Double.valueOf(itemRow.getAuthorizedQuantity()));
			noteItem.setQuantity(Double.valueOf(itemRow.getDeliveredQuantity()));
			noteItem.setUnitPrice(Double.valueOf(itemRow.getUnitPrice()));
			noteItem.setLotNumber(itemRow.getLotNumber());
			noteItem.setTokenNumber(itemRow.getTokenNumber());
			noteItem.setExpireDate(sfd.parse(itemRow.getExpireDate()));
			noteItem.setDrugPackage(
			        new DrugPackage(itemRow.getFnmCode(), Double.valueOf(itemRow.getTotalPackageUnits())));
			deliverNoteToImport.AddDeliverNoteItem(noteItem);
		}
		return deliverNoteToImport;
	}
	
	private void setDrugNameSystemDesignation(final DeliverNoteRow deliverNoteRow) {
		
		final DrugPackageService drugPackageService = Context.getService(DrugPackageService.class);
		for (final DeliverNoteItemRow item : deliverNoteRow.getItems()) {
			
			final Drug drug = drugPackageService.findDrugByDrugFNMCode(item.getFnmCode());
			item.setDrugNameSytemDesignation(drug != null ? StringUtils.upperCase(drug.getName()) : StringUtils.EMPTY);
		}
	}
}
