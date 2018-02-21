package org.openmrs.module.inventorypoc.inventor.service;

import org.openmrs.api.OpenmrsService;
import org.openmrs.module.inventorypoc.batch.service.BatchService;
import org.openmrs.module.inventorypoc.inventor.dao.InventoryDAO;
import org.openmrs.module.inventorypoc.inventor.model.Inventory;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface InventoryService extends OpenmrsService {

	public void setInventoryDAO(InventoryDAO inventoryDAO);

	void setBatchService(BatchService batchService);

	Inventory createInventory(Inventory inventory);
}
