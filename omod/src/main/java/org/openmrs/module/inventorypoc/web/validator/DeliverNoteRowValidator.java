package org.openmrs.module.inventorypoc.web.validator;

import java.text.ParseException;
import java.text.SimpleDateFormat;

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
		
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "deliveredDate", "inventorypoc.error.deliveredDate.isEmpty");
		
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "originDocument",
		    "inventorypoc.error.originDocument.isEmpty");
		
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "simamDocument", "inventorypoc.error.simamDocument.isEmpty");
		
		if (StringUtils.isNotEmpty(deliverNoteRow.getDeliveredDate())) {
			this.validateDeliveredDateFormat(deliverNoteRow.getDeliveredDate(), errors);
		}
		
		int idx = 0;
		for (final DeliverNoteItemRow item : deliverNoteRow.getItems()) {
			errors.pushNestedPath("items[" + idx + "]");
			ValidationUtils.invokeValidator(this.deliverNoteItemRowValidator, item, errors);
			errors.popNestedPath();
			idx++;
		}
	}
	
	private void validateDeliveredDateFormat(final String dateText, final Errors errors) {
		
		final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		
		try {
			sdf.parse(dateText);
		}
		catch (final ParseException e) {
			errors.rejectValue("deliveredDate", "inventorypoc.error.date.invalidDateFormat");
		}
	}
}
