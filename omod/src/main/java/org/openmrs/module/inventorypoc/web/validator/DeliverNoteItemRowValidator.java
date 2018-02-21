package org.openmrs.module.inventorypoc.web.validator;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.openmrs.Drug;
import org.openmrs.api.context.Context;
import org.openmrs.module.inventorypoc.delivernote.model.DeliverNoteItem;
import org.openmrs.module.inventorypoc.delivernote.service.DeliverNoteService;
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

		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "expirationDate",
				"inventorypoc.error.expirationDate.isEmpty");

		this.validateExistingDrugForFnmCode(item.getFnmCode(), errors);
		this.validateDeliveredDateFormat(item.getExpirationDate(), "expirationDate", errors);
		this.validateDoubleNumber(item.getTotalPackageUnits(), "totalPackageUnits", errors);
		this.validateDoubleNumber(item.getRequestedQuantity(), "requestedQuantity", errors);
		this.validateDoubleNumber(item.getAuthorizedQuantity(), "authorizedQuantity", errors);
		this.validateDoubleNumber(item.getDeliveredQuantity(), "deliveredQuantity", errors);
		this.validateDoubleNumber(item.getUnitPrice(), "unitPrice", errors);
		this.validateAuthorizedAndDeliveredQuantites(item, errors);
		this.validateRowNotLoaded(item, errors);
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

	private void validateDeliveredDateFormat(final String dateText, final String propertyAttribute,
			final Errors errors) {

		final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

		try {
			sdf.parse(dateText);
		} catch (final ParseException e) {
			errors.rejectValue(propertyAttribute, "inventorypoc.error.date.invalidDateFormat");
		}
	}

	private void validateRowNotLoaded(final DeliverNoteItemRow deliverNoteItemRow, final Errors errors) {

		final String fnmCode = deliverNoteItemRow.getFnmCode();
		final String totalPackageUnits = deliverNoteItemRow.getTotalPackageUnits();
		final String simamDocument = deliverNoteItemRow.getSimamDocument();
		final String originDocument = deliverNoteItemRow.getOriginDocument();

		if (StringUtils.isNotBlank(fnmCode) && StringUtils.isNotBlank(simamDocument)
				&& StringUtils.isNotBlank(originDocument) && NumberUtils.isNumber(totalPackageUnits)) {

			final DeliverNoteItem deliverNoteItem = Context.getService(DeliverNoteService.class)
					.findDeliverNoteItemByOriginDocumentAndSimamNumberAndDrugPackage(originDocument, simamDocument,
							fnmCode, NumberUtils.toDouble(totalPackageUnits));

			if (deliverNoteItem != null) {
				errors.rejectValue("designation", "inventorypoc.error.row.already.loaded");
			}
		}
	}
}
