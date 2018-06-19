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
import java.util.List;
import java.util.UUID;

import org.openmrs.Drug;
import org.openmrs.Location;
import org.openmrs.api.LocationService;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.inventorypoc.batch.model.Batch;
import org.openmrs.module.inventorypoc.batch.model.BatchEntry.BatchOperationType;
import org.openmrs.module.inventorypoc.batch.service.BatchService;
import org.openmrs.module.inventorypoc.delivernote.dao.DeliverNoteDAO;
import org.openmrs.module.inventorypoc.delivernote.dao.DeliverNoteItemDAO;
import org.openmrs.module.inventorypoc.delivernote.model.DeliverNote;
import org.openmrs.module.inventorypoc.delivernote.model.DeliverNoteItem;
import org.openmrs.module.inventorypoc.delivernote.validation.DeliverNoteValidator;
import org.openmrs.module.inventorypoc.drugpackage.model.DrugPackage;
import org.openmrs.module.inventorypoc.drugpackage.service.DrugPackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class DeliverNoteServiceImpl extends BaseOpenmrsService implements DeliverNoteService {
	
	private DeliverNoteDAO deliverNoteDAO;
	
	private DeliverNoteItemDAO deliverNoteItemDAO;
	
	private BatchService batchService;
	
	private DrugPackageService drugPackageService;
	
	private LocationService locationService;
	
	@Autowired
	private DeliverNoteValidator deliverNoteValidator;
	
	@Override
	public void setDeliverNoteDAO(final DeliverNoteDAO deliverNoteDAO) {
		this.deliverNoteDAO = deliverNoteDAO;
	}
	
	@Override
	public void setDeliverNoteItemDAO(final DeliverNoteItemDAO deliverNoteItemDAO) {
		this.deliverNoteItemDAO = deliverNoteItemDAO;
	}
	
	@Override
	public void setDrugPackageService(final DrugPackageService drugPackageService) {
		this.drugPackageService = drugPackageService;
	}
	
	@Override
	public void setBatchService(final BatchService batchService) {
		this.batchService = batchService;
	}
	
	@Override
	public void setLocationService(final LocationService locationService) {
		this.locationService = locationService;
	}
	
	@Override
	public DeliverNote createDeliverNote(final DeliverNote deliverNote) {
		
		this.deliverNoteValidator.validate(deliverNote);
		
		final Location location = this.locationService.getLocation(deliverNote.getLocation().getLocationId());
		DeliverNote foundDeliverNote = this.deliverNoteDAO
		        .findBySimamNumberAndDeliveryDate(deliverNote.getSimamNumber(), deliverNote.getDeliveryDate(), false);
		
		if (foundDeliverNote == null) {
			deliverNote.setLocation(location);
			deliverNote.setUuid(UUID.randomUUID().toString());
			foundDeliverNote = this.deliverNoteDAO.save(deliverNote);
		}
		
		for (final DeliverNoteItem noteItem : deliverNote.getDeliverNoteItems()) {
			
			final DrugPackage drugPackage = this.drugPackageService
			        .fingDrugPackageById(noteItem.getDrugPackage().getId());
			
			final Batch batch = this.generateBatch(noteItem, drugPackage, location);
			
			final DeliverNoteItem deliverNoteItem = new DeliverNoteItem();
			deliverNoteItem.setBatch(batch);
			deliverNoteItem.setDeliverNote(foundDeliverNote);
			deliverNoteItem.setQuantity(noteItem.getQuantity());
			deliverNoteItem.setDrugPackage(drugPackage);
			deliverNoteItem.setUnitPrice(noteItem.getUnitPrice());
			deliverNoteItem.setUuid(UUID.randomUUID().toString());
			deliverNoteItem.setAuthorizedQuantity(noteItem.getAuthorizedQuantity());
			deliverNoteItem.setRequestedQuantity(noteItem.getRequestedQuantity());
			deliverNoteItem.setLotNumber(noteItem.getLotNumber());
			deliverNoteItem.setTokenNumber(noteItem.getTokenNumber());
			deliverNoteItem.setExpireDate(noteItem.getExpireDate());
			
			this.deliverNoteItemDAO.save(deliverNoteItem);
		}
		return foundDeliverNote;
	}
	
	private Batch generateBatch(final DeliverNoteItem noteItem, final DrugPackage drugPackage,
	        final Location location) {
		
		final DeliverNoteItem foundDeliverNoteItem = this.findDeliverNoteItemByDrugAndLotNumber(drugPackage.getDrug(),
		    noteItem.getLotNumber());
		
		if (foundDeliverNoteItem != null) {
			return this.batchService.adjustBatchWithDeliverNoteItem(foundDeliverNoteItem.getBatch(), noteItem);
		}
		
		final Batch batch = new Batch();
		batch.setExpireDate(noteItem.getExpireDate());
		batch.setDrugPackage(noteItem.getDrugPackage());
		batch.setPackageQuantity(noteItem.getQuantity());
		batch.setPackageQuantityUnits(noteItem.getQuantity() * drugPackage.getTotalQuantity());
		batch.setLocation(location);
		batch.setRemainPackageQuantityUnits(batch.getPackageQuantityUnits());
		batch.setUnBalancedUnitsQuantity(0d);
		batch.setVersion(1d);
		
		return this.batchService.createBatch(batch, BatchOperationType.RECEIPT);
	}
	
	@Override
	public void importDeliverNote(final DeliverNote deliverNote) {
		
		for (final DeliverNoteItem item : deliverNote.getDeliverNoteItems()) {
			
			final DrugPackage drugPackage = this.findOrCreateDrugPackage(item.getDrugPackage());
			item.setDrugPackage(drugPackage);
		}
		this.createDeliverNote(deliverNote);
	}
	
	private DrugPackage findOrCreateDrugPackage(final DrugPackage drugPackage) {
		
		final Drug drug = this.drugPackageService.findDrugByDrugFNMCode(drugPackage.getBarcode());
		
		final DrugPackage foundDrugPackage = this.drugPackageService.findDrugPackageByDrugAndTotalQuantity(drug,
		    drugPackage.getTotalQuantity());
		
		if (foundDrugPackage == null) {
			drugPackage.setDrug(drug);
			return this.drugPackageService.saveDrugPackage(drugPackage);
		}
		return foundDrugPackage;
	}
	
	@Override
	public DeliverNote findBySimamNumberAndDeliveryDate(final String simamNumber, final Date deliveryDate) {
		return this.deliverNoteDAO.findBySimamNumberAndDeliveryDate(simamNumber, deliveryDate, false);
	}
	
	@Override
	public DeliverNoteItem findDeliverNoteItemByTokenNumber(final String tokenNumber) {
		
		return this.deliverNoteItemDAO.findByTokenNumber(tokenNumber, false);
	}
	
	@Override
	public DeliverNoteItem findDeliverNoteItemByLotAndTokenNumber(final String lotNumber, final String tokenNumber) {
		return this.deliverNoteItemDAO.findByLotAndTokenNumber(lotNumber, tokenNumber, false);
	}
	
	@Override
	public DeliverNoteItem findDeliverNoteItemByDrugAndLotNumber(final Drug drug, final String lotNumber) {
		return this.deliverNoteItemDAO.findByDrugAndLotNumber(drug, lotNumber, false);
	}
	
	@Override
	public List<DeliverNoteItem> findDeliverNoteItemsByBatchAndDrugPackage(final Batch batch,
	        final DrugPackage drugPackage) {
		return this.deliverNoteItemDAO.findByBatchAndDrugPackage(batch, drugPackage, false);
	}
}
