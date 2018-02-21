package org.openmrs.module.inventorypoc.web.validator;

import org.openmrs.module.inventorypoc.web.bean.StockAdjust;
import org.openmrs.module.inventorypoc.web.bean.ItemInventory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class InventoryValidator implements Validator {
	
	@Autowired
	private ItemInventoryValidator itemInventoryValidator;
	
	@Override
	public boolean supports(final Class<?> clazz) {
		return StockAdjust.class.isAssignableFrom(clazz);
	}
	
	@Override
	public void validate(final Object target, final Errors errors) {
		final StockAdjust inventory = (StockAdjust) target;
		
		int idx = 0;
		for (final ItemInventory item : inventory.getItems()) {
			errors.pushNestedPath("items[" + idx + "]");
			ValidationUtils.invokeValidator(this.itemInventoryValidator, item, errors);
			errors.popNestedPath();
			idx++;
		}
	}
}
