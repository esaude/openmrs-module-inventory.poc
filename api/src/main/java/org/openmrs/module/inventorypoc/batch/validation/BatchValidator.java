package org.openmrs.module.inventorypoc.batch.validation;

import org.openmrs.api.APIException;
import org.openmrs.api.context.Context;
import org.openmrs.module.inventorypoc.batch.model.Batch;
import org.springframework.stereotype.Component;

@Component
public class BatchValidator {
	
	public void validateAdjustments(final Batch batch, final Double newValue) {
		
		final Double delta = newValue - batch.getRemainPackageQuantityUnits();
		
		final Double valueAdjusted = batch.getRemainPackageQuantityUnits() + delta;
		
		if (valueAdjusted > batch.getPackageQuantityUnits()) {
			
			throw new APIException(Context.getMessageSourceService().getMessage(
			    "inventorypoc.error.batchentryadjustment.AdjustQuantityCannotBeGreaterThanInitialQuantity",
			    new String[] { batch.getPackageQuantityUnits().toString(), batch.getId().toString() },
			    Context.getLocale()));
		}
	}
}
