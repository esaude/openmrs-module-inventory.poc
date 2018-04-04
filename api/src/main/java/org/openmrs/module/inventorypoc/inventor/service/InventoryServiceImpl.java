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
package org.openmrs.module.inventorypoc.inventor.service;

import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.inventorypoc.batch.model.Batch;
import org.openmrs.module.inventorypoc.batch.service.BatchService;
import org.openmrs.module.inventorypoc.inventor.dao.InventoryDAO;
import org.openmrs.module.inventorypoc.inventor.model.Inventory;

public class InventoryServiceImpl extends BaseOpenmrsService implements InventoryService {
	
	private InventoryDAO inventoryDAO;
	
	private BatchService batchService;
	
	@Override
	public void setInventoryDAO(final InventoryDAO inventoryDAO) {
		this.inventoryDAO = inventoryDAO;
	}
	
	@Override
	public void setBatchService(final BatchService batchService) {
		this.batchService = batchService;
	}
	
	@Override
	public Inventory createInventory(final Inventory inventory) {
		
		final Batch batch = inventory.getBatch();
		
		inventory.setSystemCount(batch.getRemainPackageQuantityUnits());
		
		this.batchService.adjustBatchCurrentQuantity(batch, inventory.getPhisicalCount());
		return this.inventoryDAO.save(inventory);
	}
}
