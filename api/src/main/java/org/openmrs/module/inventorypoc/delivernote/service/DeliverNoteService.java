/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.module.inventorypoc.delivernote.service;

import java.util.Date;

import org.openmrs.Drug;
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
	
	public DeliverNote findBySimamNumberAndDeliveryDate(String simamNumber, Date deliveryDate);
	
	public DeliverNoteItem findDeliverNoteItemByTokenNumber(String tokenNumber);
	
	public DeliverNoteItem findDeliverNoteItemByLotAndTokenNumber(String lotNumber, String tokenNumber);
	
	public DeliverNoteItem findDeliverNoteItemByDrugAndLotNumber(Drug drug, String lotNumber);
	
}
