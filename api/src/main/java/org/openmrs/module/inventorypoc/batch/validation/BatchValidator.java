package org.openmrs.module.inventorypoc.batch.validation;

import java.util.Date;

import org.openmrs.DrugOrder;
import org.openmrs.Location;
import org.openmrs.api.APIException;
import org.openmrs.api.context.Context;
import org.openmrs.module.inventorypoc.batch.model.Batch;
import org.openmrs.module.inventorypoc.batch.service.BatchService;
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
	
	public void validateWaste(final DrugOrder drugOrder, final Location location, final Double quantity,
	        final Date date) {
		
		final Double currentStockBalance = Context.getService(BatchService.class)
		        .findCurrentStockBalance(drugOrder.getDrug(), location, date);
		
		if (currentStockBalance < quantity) {
			
			throw new APIException(Context.getMessageSourceService().getMessage(
			    "inventorypoc.error.insufficient.stock.for.dispensation",
			    new String[] { drugOrder.getDrug().getDisplayName(), String.valueOf(currentStockBalance) },
			    Context.getLocale()));
			
		}
	}
}
