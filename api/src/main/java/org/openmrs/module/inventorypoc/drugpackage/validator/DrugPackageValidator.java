/**
 *
 */
package org.openmrs.module.inventorypoc.drugpackage.validator;

import org.openmrs.annotation.Handler;
import org.openmrs.module.inventorypoc.drugpackage.model.DrugPackage;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Handler(supports = { DrugPackage.class })
public class DrugPackageValidator implements Validator {
	
	@Override
	public boolean supports(final Class<?> clazz) {
		return DrugPackage.class.isAssignableFrom(clazz);
	}
	
	@Override
	public void validate(final Object target, final Errors errors) {
		final DrugPackage drugpackage = (DrugPackage) target;
		if (drugpackage == null) {
			errors.reject("error.general");
		} else {
			// for the following elements Order.hbm.xml says: not-null="true"
			// ValidationUtils.rejectIfEmpty(errors, "voided", "error.null");
			// //For DrugOrders, the api will set the concept to drug.concept
			// if (!DrugOrder.class.isAssignableFrom(order.getClass())) {
			// ValidationUtils.rejectIfEmpty(errors, "concept",
			// "Concept.noConceptSelected");
			// }
			// ValidationUtils.rejectIfEmpty(errors, "patient", "error.null");
			// ValidationUtils.rejectIfEmpty(errors, "encounter", "error.null");
			// ValidationUtils.rejectIfEmpty(errors, "orderer", "error.null");
			// ValidationUtils.rejectIfEmpty(errors, "urgency", "error.null");
			// ValidationUtils.rejectIfEmpty(errors, "action", "error.null");
			//
			// validateSamePatientInOrderAndEncounter(order, errors);
			// validateOrderTypeClass(order, errors);
			// validateDateActivated(order, errors);
			// validateScheduledDate(order, errors);
			// ValidateUtil.validateFieldLengths(errors, obj.getClass(),
			// "orderReasonNonCoded", "accessionNumber",
			// "commentToFulfiller", "voidReason");
			//
			// validateOrderGroupEncounter(order, errors);
			// validateOrderGroupPatient(order, errors);
		}
		
	}
}
