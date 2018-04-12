package org.openmrs.module.inventorypoc.delivernote.validation;

import org.openmrs.api.APIException;
import org.openmrs.api.context.Context;
import org.openmrs.module.inventorypoc.batch.model.Batch;
import org.openmrs.module.inventorypoc.delivernote.model.DeliverNote;
import org.openmrs.module.inventorypoc.delivernote.model.DeliverNoteItem;
import org.openmrs.module.inventorypoc.delivernote.service.DeliverNoteService;
import org.springframework.stereotype.Component;

@Component
public class DeliverNoteValidator {
	
	public void validate(final DeliverNote deliverNote) {
		
		final DeliverNoteService deliverNoteService = Context.getService(DeliverNoteService.class);
		
		for (final DeliverNoteItem noteItem : deliverNote.getDeliverNoteItems()) {
			
			final DeliverNoteItem foundByToken = deliverNoteService
			        .findDeliverNoteItemByTokenNumber(noteItem.getTokenNumber());
			
			if (foundByToken != null) {
				final String errorMessage = Context.getMessageSourceService().getMessage(
				    "inventorypoc.error.delivernoteitem.already.exists",
				    new String[] { noteItem.getTokenNumber(), noteItem.getLotNumber() }, Context.getLocale());
				throw new APIException(errorMessage);
			}
			
			final DeliverNoteItem foundItemByDrugPackage = deliverNoteService.findDeliverNoteItemByDrugAndLotNumber(
			    noteItem.getDrugPackage().getDrug(), noteItem.getLotNumber());
			
			if (foundItemByDrugPackage != null) {
				
				if (!noteItem.getExpireDate().equals(foundItemByDrugPackage.getExpireDate())) {
					
					throw new APIException(
					        Context.getMessageSourceService().getMessage(
					            "inventorypoc.error.importdelivernote.expirationdate.doesnotmatch.tothepreviousone",
					            new String[] { foundItemByDrugPackage.getExpireDate().toString(),
					                    noteItem.getLotNumber(), noteItem.getDrugPackage().getDrug().getName() },
					            Context.getLocale()));
				}
				
				if (noteItem.getAuthorizedQuantity().compareTo(foundItemByDrugPackage.getAuthorizedQuantity()) != 0) {
					
					throw new APIException(Context.getMessageSourceService().getMessage(
					    "inventorypoc.error.importdelivernote.authorizedquantity.doesnotmatch.tothepreviousone",
					    new String[] { foundItemByDrugPackage.getAuthorizedQuantity().toString(),
					            noteItem.getLotNumber(), noteItem.getDrugPackage().getDrug().getName() },
					    Context.getLocale()));
				}
				
				final Batch batch = foundItemByDrugPackage.getBatch();
				
				final Double totalToDeliver = foundItemByDrugPackage.getAuthorizedQuantity()
				        - batch.getPackageQuantity();
				
				if (noteItem.getQuantity().compareTo(totalToDeliver) > 0) {
					throw new APIException(Context.getMessageSourceService().getMessage(
					    "inventorypoc.error.importdelivernote.quantitytodeliver.exceeds.authorized",
					    new String[] { foundItemByDrugPackage.getAuthorizedQuantity().toString(),
					            noteItem.getLotNumber(), noteItem.getDrugPackage().getDrug().getName() },
					    Context.getLocale()));
				}
			}
		}
	}
}
