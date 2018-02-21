package org.openmrs.module.inventorypoc.delivernote.service;

import org.openmrs.api.LocationService;
import org.openmrs.api.OpenmrsService;
import org.openmrs.module.inventorypoc.batch.service.BatchService;
import org.openmrs.module.inventorypoc.delivernote.dao.DeliverNoteDAO;
import org.openmrs.module.inventorypoc.delivernote.dao.DeliverNoteItemDAO;
import org.openmrs.module.inventorypoc.delivernote.model.DeliverNote;
import org.openmrs.module.inventorypoc.delivernote.model.DeliverNoteItem;
import org.openmrs.module.inventorypoc.drugpackage.service.DrugPackageService;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface DeliverNoteService extends OpenmrsService {
	
	public void setDeliverNoteDAO(DeliverNoteDAO deliverNoteDAO);
	
	public void setDeliverNoteItemDAO(DeliverNoteItemDAO deliverNoteItemDAO);
	
	public void setDrugPackageService(DrugPackageService drugPackageService);
	
	public void setBatchService(BatchService batchService);
	
	public void setLocationService(LocationService locationService);
	
	public DeliverNote createDeliverNote(DeliverNote deliverNote);
	
	public void importDeliverNote(DeliverNote deliverNote);
	
	public DeliverNote findDeliverNoteByOriginDocumentAndSimamNumber(String originDocument, String simamNumber);
	
	public DeliverNoteItem findDeliverNoteItemByOriginDocumentAndSimamNumberAndDrugPackage(final String originDocument,
	        final String simamNumber, final String drugFnmCode, final Double totalQuantityPerPackage);
}
