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
