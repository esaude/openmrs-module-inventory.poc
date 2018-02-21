/**
 *
 */
package org.openmrs.module.inventorypoc.batch.service;

import java.util.Date;
import java.util.List;

import org.openmrs.Drug;
import org.openmrs.DrugOrder;
import org.openmrs.EncounterType;
import org.openmrs.Location;
import org.openmrs.api.context.Context;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.inventorypoc.batch.dao.BatchDAO;
import org.openmrs.module.inventorypoc.batch.dao.BatchEntryDAO;
import org.openmrs.module.inventorypoc.batch.model.Batch;
import org.openmrs.module.inventorypoc.batch.model.BatchEntry;
import org.openmrs.module.inventorypoc.batch.model.BatchEntry.BatchOperationType;
import org.openmrs.module.inventorypoc.common.util.MappedEncounterTypes;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class BatchServiceImpl extends BaseOpenmrsService implements BatchService {
	
	private BatchDAO batchDAO;
	
	private BatchEntryDAO batchEntryDAO;
	
	private static final Double ZERO_DOUBLE_VALUE = Double.valueOf(0);
	
	@Override
	public void setBatchDAO(final BatchDAO batchDAO) {
		this.batchDAO = batchDAO;
	}
	
	@Override
	public void setBatchEntryDAO(final BatchEntryDAO batchEntryDAO) {
		this.batchEntryDAO = batchEntryDAO;
	}
	
	@Override
	public Batch createBatch(final Batch batch, final BatchOperationType batchOperationType) {
		
		this.batchDAO.save(batch);
		final BatchEntry batchEntry = new BatchEntry(batch, batchOperationType, batch.getPackageQuantityUnits());
		this.batchEntryDAO.save(batchEntry);
		
		return batch;
	}
	
	@Override
	public void createWasteDrug(final DrugOrder drugOrder, final Location location, final Double quantity,
	        final Date date) throws Exception {
		
		final List<Batch> batches = this.batchDAO.findByDrugAndLocationAndNotExpiredDate(drugOrder.getDrug(), location,
		    date, false);
		
		if (batches.isEmpty()) {
			// TODO: should find a way to waste drugs without initial inventory
		} else {
			this.processForExistingBatches(drugOrder, batches, quantity);
		}
	}
	
	private void processForExistingBatches(final DrugOrder drugOrder, final List<Batch> batches,
	        final Double quantity) {
		Double wastedQuantity = quantity;
		
		for (final Batch batch : batches) {
			if (wastedQuantity.compareTo(BatchServiceImpl.ZERO_DOUBLE_VALUE) > 0) {
				
				Double entryQuantity;
				if (wastedQuantity.compareTo(batch.getRemainPackageQuantityUnits()) > 0) {
					entryQuantity = wastedQuantity - batch.getRemainPackageQuantityUnits();
				} else {
					entryQuantity = wastedQuantity;
				}
				this.batchEntryDAO.create(drugOrder, batch, BatchOperationType.DISTRIBUTION, entryQuantity);
				this.batchDAO.decreaseRemainPackageQuantityUnits(batch, entryQuantity);
				wastedQuantity = wastedQuantity - entryQuantity;
				
			} else {
				break;
			}
		}
	}
	
	@Override
	public void reverseWastedDrug(final DrugOrder drugOrder) throws Exception {
		
		final EncounterType encounterType = Context.getEncounterService()
		        .getEncounterTypeByUuid(MappedEncounterTypes.DISPENSATION_ENCOUNTER_TYPE);
		
		final BatchEntry batchEntry = this.batchEntryDAO.findByDispensedOrder(drugOrder, encounterType, false);
		this.batchEntryDAO.reverse(batchEntry);
	}
	
	@Override
	public List<Batch> findBatchesByDrugAndLocationAndNotExpiredDate(final Drug drug, final Location location,
	        final Date date) {
		return this.batchDAO.findByDrugAndLocationAndNotExpiredDate(drug, location, date, false);
	}
	
	@Override
	public List<Batch> findAllAvailableStock(final Location location, final Date currentDate) {
		
		return this.batchDAO.findByLocationAndAvailableQuantity(location, currentDate, false);
	}
	
	@Override
	public Batch adjustBatchCurrentQuantity(final Batch batch, final Double newRemainPackageQuantityUnits) {
		
		batch.setRemainPackageQuantityUnits(newRemainPackageQuantityUnits);
		
		final BatchEntry batchEntry = new BatchEntry(batch, BatchOperationType.ADJUSTMENT,
		        newRemainPackageQuantityUnits);
		this.batchEntryDAO.save(batchEntry);
		this.batchDAO.save(batch);
		
		return batch;
	}
}
