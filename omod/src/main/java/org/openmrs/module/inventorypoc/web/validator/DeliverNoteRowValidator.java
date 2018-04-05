package org.openmrs.module.inventorypoc.web.validator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.openmrs.module.inventorypoc.web.bean.DeliverNoteItemRow;
import org.openmrs.module.inventorypoc.web.bean.DeliverNoteRow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * Validates the {@link Package}
 */
@Component
public class DeliverNoteRowValidator implements Validator {
	
	@Autowired
	private DeliverNoteItemRowValidator deliverNoteItemRowValidator;
	
	private static final String DATE_REGEX_PATTERN = "([0-3]{1}[0-9]{1})/([0-1]{1}[0-9]{1})/([1-3]{1}[0-9]{3})";
	
	/**
	 * @see org.springframework.validation.Validator#supports(java.lang.Class)
	 */
	@Override
	public boolean supports(@SuppressWarnings("rawtypes") final Class clazz) {
		return DeliverNoteRow.class.isAssignableFrom(clazz);
	}
	
	/**
	 * @see org.springframework.validation.Validator#validate(java.lang.Object,
	 *      org.springframework.validation.Errors)
	 * @should reject empty name
	 * @should reject empty description
	 * @should reject empty date created
	 * @should reject empty non-empty group and empty version
	 * @should reject too long name
	 * @should reject too long description
	 * @should reject package version if not subsequent incremental version
	 */
	@Override
	public void validate(final Object obj, final Errors errors) {
		
		final DeliverNoteRow deliverNoteRow = (DeliverNoteRow) obj;
		
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "healthFacilityName",
		    "inventorypoc.error.deliverNote.healthFacilityName.isEmpty");
		
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "deliveryDate", "inventorypoc.error.deliveredDate.isEmpty");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "simamNumber", "inventorypoc.error.simamDocument.isEmpty");
		
		if (StringUtils.isNotEmpty(deliverNoteRow.getDeliveryDate())) {
			DeliverNoteRowValidator.validateDateFormat("deliveryDate", deliverNoteRow.getDeliveryDate(), errors);
		}
		
		try {
			this.validateDatesComparison(deliverNoteRow, errors);
		}
		catch (final ParseException e) {}
		
		int idx = 0;
		for (final DeliverNoteItemRow item : deliverNoteRow.getItems()) {
			errors.pushNestedPath("items[" + idx + "]");
			ValidationUtils.invokeValidator(this.deliverNoteItemRowValidator, item, errors);
			errors.popNestedPath();
			idx++;
		}
	}
	
	public static void validateDateFormat(final String propertyName, final String dateText, final Errors errors) {
		
		if (!DeliverNoteRowValidator.isDateValid(dateText)) {
			errors.rejectValue(propertyName, "inventorypoc.error.date.invalidDateFormat");
		}
	}
	
	private void validateDatesComparison(final DeliverNoteRow deliverNote, final Errors errors) throws ParseException {
		
		if (DeliverNoteRowValidator.isDateValid(deliverNote.getDeliveryDate())) {
			
			final Date deliveredDate = DeliverNoteRowValidator.getDateFromString(deliverNote.getDeliveryDate());
			
			if (deliveredDate.after(new Date())) {
				
				errors.rejectValue("deliveryDate", "inventorypoc.error.deliveredDate.isGreaterThanCurrentDate");
			}
		}
	}
	
	public static boolean isDateValid(final String dateText) {
		try {
			DeliverNoteRowValidator.getDateFromString(dateText);
			return true && DeliverNoteRowValidator.isDatePatternValid(dateText);
		}
		catch (final ParseException e) {
			return false;
		}
	}
	
	public static Date getDateFromString(final String dateText) throws ParseException {
		final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		return sdf.parse(dateText);
	}
	
	private static boolean isDatePatternValid(final String dateStr) {
		
		return dateStr != null ? dateStr.matches(DeliverNoteRowValidator.DATE_REGEX_PATTERN) : false;
	}
}
