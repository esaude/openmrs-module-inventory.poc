package org.openmrs.module.inventorypoc.web.validator;

import org.openmrs.module.inventorypoc.web.bean.ItemInventory;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class ItemInventoryValidator implements Validator {

	@Override
	public boolean supports(final Class<?> clazz) {
		return ItemInventory.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(final Object target, final Errors errors) {

		final ItemInventory item = (ItemInventory) target;

		if (item.isSelected()) {

			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "newQuantity",
					"inventorypoc.stockAdjustment.error.newQuantityEmpy");

			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "reason", "inventorypoc.stockAdjustment.error.reason");
			this.validateDoubleNumber(item.getNewQuantity(), errors);
			this.validateNewQuantityMaxAllowed(item, errors);
		}
	}

	private void validateDoubleNumber(final Double newQuantity, final Errors errors) {

		if ((newQuantity != null) && (newQuantity < 0.0)) {
			errors.rejectValue("newQuantity", "inventorypoc.error.fieldIsNotPosetiveNumber");
		}
	}

	private void validateNewQuantityMaxAllowed(final ItemInventory item, final Errors errors) {

		if (item.getNewQuantity() != null) {
			final Double packageQuantityUnits = item.getBatch().getPackageQuantityUnits();
			if (packageQuantityUnits.compareTo(item.getNewQuantity()) < 0) {
				errors.rejectValue("newQuantity",
						"inventorypoc.stockAdjustment.error.newQuantityGreaterThanInitialQuantity",
						new Double[] { packageQuantityUnits }, null);
			}
		}
	}
}
