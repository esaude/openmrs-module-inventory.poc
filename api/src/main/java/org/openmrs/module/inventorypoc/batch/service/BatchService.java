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
package org.openmrs.module.inventorypoc.batch.service;

import java.util.Date;
import java.util.List;

import org.openmrs.Drug;
import org.openmrs.DrugOrder;
import org.openmrs.Location;
import org.openmrs.api.OpenmrsService;
import org.openmrs.module.inventorypoc.batch.dao.BatchDAO;
import org.openmrs.module.inventorypoc.batch.dao.BatchEntryDAO;
import org.openmrs.module.inventorypoc.batch.model.Batch;
import org.openmrs.module.inventorypoc.batch.model.BatchEntry.BatchOperationType;
import org.openmrs.module.inventorypoc.delivernote.model.DeliverNoteItem;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface BatchService extends OpenmrsService {
	
	public void setBatchDAO(BatchDAO batchDAO);
	
	public void setBatchEntryDAO(BatchEntryDAO batchEntryDAO);
	
	public Batch createBatch(Batch batch, BatchOperationType batchOperationType);
	
	public Batch adjustBatchWithDeliverNoteItem(Batch batch, DeliverNoteItem deliverNoteItem);
	
	public Batch adjustBatchCurrentQuantity(Batch batch, Double newRemainPackageQuantityUnits);
	
	public void createWasteDrug(DrugOrder drugOrder, Location location, Double quantity, Date date) throws Exception;
	
	public void reverseWastedDrug(DrugOrder drugOrder) throws Exception;
	
	public List<Batch> findBatchesByDrugAndLocationAndNotExpiredDate(Drug drug, Location location, Date date);
	
	Double findCurrentStockBalance(Drug drug, Location location, Date date);
	
	public List<Batch> findAllAvailableStock(Location location, Date currentDate);
	
	Batch findBatchById(Integer batchId);
}
