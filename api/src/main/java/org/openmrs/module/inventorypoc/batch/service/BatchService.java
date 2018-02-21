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
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface BatchService extends OpenmrsService {
	
	public void setBatchDAO(BatchDAO batchDAO);
	
	public void setBatchEntryDAO(BatchEntryDAO batchEntryDAO);
	
	public Batch createBatch(Batch batch, BatchOperationType batchOperationType);
	
	public Batch adjustBatchCurrentQuantity(Batch batch, Double newRemainPackageQuantityUnits);
	
	public void createWasteDrug(DrugOrder drugOrder, Location location, Double quantity, Date date) throws Exception;
	
	public void reverseWastedDrug(DrugOrder drugOrder) throws Exception;
	
	public List<Batch> findBatchesByDrugAndLocationAndNotExpiredDate(Drug drug, Location location, Date date);
	
	public List<Batch> findAllAvailableStock(Location location, Date currentDate);
	
}
