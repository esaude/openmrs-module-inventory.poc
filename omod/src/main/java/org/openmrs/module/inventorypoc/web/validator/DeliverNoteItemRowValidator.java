package org.openmrs.module.inventorypoc.web.validator;

import java.text.ParseException;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.openmrs.Drug;
import org.openmrs.api.context.Context;
import org.openmrs.module.inventorypoc.delivernote.model.DeliverNoteItem;
import org.openmrs.module.inventorypoc.delivernote.service.DeliverNoteService;
import org.openmrs.module.inventorypoc.drugpackage.model.DrugPackage;
import org.openmrs.module.inventorypoc.drugpackage.service.DrugPackageService;
import org.openmrs.module.inventorypoc.web.bean.DeliverNoteItemRow;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class DeliverNoteItemRowValidator implements Validator {
	
	@Override
	public boolean supports(final Class<?> clazz) {
		return DeliverNoteItemRow.class.isAssignableFrom(clazz);
	}
	
	@Override
	public void validate(final Object target, final Errors errors) {
		
		final DeliverNoteItemRow item = (DeliverNoteItemRow) target;
		
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "fnmCode", "inventorypoc.error.fnmCode.isEmpty");
		
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "designation", "inventorypoc.error.designation.isEmpty");
		
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "totalPackageUnits",
		    "inventorypoc.error.totalPackageUnits.isEmpty");
		
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "requestedQuantity",
		    "inventorypoc.error.requestedQuantity.isEmpty");
		
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "authorizedQuantity",
		    "inventorypoc.error.authorizedQuantity.isEmpty");
		
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "deliveredQuantity",
		    "inventorypoc.error.deliveredQuantity.isEmpty");
		
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "unitPrice", "inventorypoc.error.unitPrice.isEmpty");
		
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "lotNumber", "inventorypoc.error.lotNumber.isEmpty");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "tokenNumber", "inventorypoc.error.tokenNumber.isEmpty");
		
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "expireDate", "inventorypoc.error.expireDate.isEmpty");
		
		DeliverNoteRowValidator.validateDateFormat("expireDate", item.getExpireDate(), errors);
		
		try {
			this.validateDatesComparison(item, errors);
			
		}
		catch (final Exception e) {}
		
		this.validateExistingDrugForFnmCode(item.getFnmCode(), errors);
		this.validateDoubleNumber(item.getTotalPackageUnits(), "totalPackageUnits", errors);
		this.validateDoubleNumber(item.getRequestedQuantity(), "requestedQuantity", errors);
		this.validateDoubleNumber(item.getAuthorizedQuantity(), "authorizedQuantity", errors);
		this.validateDoubleNumber(item.getDeliveredQuantity(), "deliveredQuantity", errors);
		this.validateDoubleNumber(item.getUnitPrice(), "unitPrice", errors);
		this.validateAuthorizedAndDeliveredQuantites(item, errors);
		this.validateExistingDrugPackage(item.getFnmCode(), item.getTotalPackageUnits(), errors);
		this.validateRowNotLoaded(item, errors);
	}
	
	private void validateDatesComparison(final DeliverNoteItemRow deliverNote, final Errors errors)
	        throws ParseException {
		
		if (DeliverNoteRowValidator.isDateValid(deliverNote.getExpireDate())) {
			
			final Date deliveredDate = DeliverNoteRowValidator.getDateFromString(deliverNote.getExpireDate());
			
			if (deliveredDate.before(new Date())) {
				
				errors.rejectValue("expireDate", "inventorypoc.error.expireDate.mustBe.greaterThan.currentDate");
			}
		}
	}
	
	private void validateExistingDrugForFnmCode(final String fnmCode, final Errors errors) {
		final DrugPackageService drugPackageService = Context.getService(DrugPackageService.class);
		if (StringUtils.isNotBlank(fnmCode)) {
			final Drug drugByFnmCode = drugPackageService.findDrugByDrugFNMCode(fnmCode);
			if (drugByFnmCode == null) {
				errors.rejectValue("fnmCode", "inventorypoc.error.drug.notFoundForFnmCode", new String[] { fnmCode },
				    null);
			}
		}
	}
	
	private void validateAuthorizedAndDeliveredQuantites(final DeliverNoteItemRow item, final Errors errors) {
		
		if (NumberUtils.isNumber(item.getAuthorizedQuantity()) && NumberUtils.isNumber(item.getDeliveredQuantity())) {
			
			final double authorizedQuantity = NumberUtils.toDouble(item.getAuthorizedQuantity());
			
			final double deliveredQuantity = NumberUtils.toDouble(item.getDeliveredQuantity());
			
			if (deliveredQuantity > authorizedQuantity) {
				errors.rejectValue("deliveredQuantity",
				    "inventorypoc.error.deliveredQuantity.shouldBeLessOrEqualToAuthorizedQuantity",
				    new Double[] { authorizedQuantity }, null);
			}
		}
	}
	
	private void validateDoubleNumber(final String dateText, final String propertyAttribute, final Errors errors) {
		if (StringUtils.isNotBlank(dateText)) {
			if (!NumberUtils.isNumber(dateText)) {
				errors.rejectValue(propertyAttribute, "inventorypoc.error.fieldIsNotNumber");
			} else {
				final double doubleValue = NumberUtils.toDouble(dateText);
				
				if (doubleValue <= 0.0) {
					errors.rejectValue(propertyAttribute, "inventorypoc.error.fieldIsNotPosetiveNumber");
				}
			}
		}
	}
	
	private void validateRowNotLoaded(final DeliverNoteItemRow deliverNoteItemRow, final Errors errors) {
		
		final String tokenNumber = deliverNoteItemRow.getTokenNumber();
		
		if (StringUtils.isNotBlank(tokenNumber)) {
			
			final DeliverNoteItem deliverNoteItem = Context.getService(DeliverNoteService.class)
			        .findDeliverNoteItemByTokenNumber(tokenNumber);
			
			if (deliverNoteItem != null) {
				errors.rejectValue("designation", "inventorypoc.error.row.already.loaded");
			}
		}
	}
	
	private void validateExistingDrugPackage(final String fnmCode, final String totalQuantityText,
	        final Errors errors) {
		
		if (NumberUtils.isNumber(totalQuantityText)) {
			
			final double totalQuantity = NumberUtils.toDouble(totalQuantityText);
			final DrugPackageService drugPackageService = Context.getService(DrugPackageService.class);
			
			if (StringUtils.isNotBlank(fnmCode)) {
				
				final Drug drug = drugPackageService.findDrugByDrugFNMCode(fnmCode);
				
				if (drug != null) {
					
					final DrugPackage drugPackage = drugPackageService.findDrugPackageByDrug(drug);
					
					if ((drugPackage != null) && (drugPackage.getTotalQuantity().compareTo(totalQuantity) != 0)) {
						
						errors.rejectValue(
						    "totalPackageUnits", "inventorypoc.error.drugpackage.already.exists", new String[] {
						            drug.getDisplayName(), fnmCode, drugPackage.getTotalQuantity().toString() },
						    null);
					}
				}
			}
		}
	}
}
