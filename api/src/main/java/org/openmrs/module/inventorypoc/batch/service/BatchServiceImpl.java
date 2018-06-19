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
import org.openmrs.EncounterType;
import org.openmrs.Location;
import org.openmrs.api.APIException;
import org.openmrs.api.context.Context;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.inventorypoc.batch.dao.BatchDAO;
import org.openmrs.module.inventorypoc.batch.dao.BatchEntryDAO;
import org.openmrs.module.inventorypoc.batch.model.Batch;
import org.openmrs.module.inventorypoc.batch.model.BatchEntry;
import org.openmrs.module.inventorypoc.batch.model.BatchEntry.BatchOperationType;
import org.openmrs.module.inventorypoc.batch.validation.BatchValidator;
import org.openmrs.module.inventorypoc.common.util.MappedEncounterTypes;
import org.openmrs.module.inventorypoc.delivernote.model.DeliverNoteItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class BatchServiceImpl extends BaseOpenmrsService implements BatchService {

	private BatchDAO batchDAO;

	private BatchEntryDAO batchEntryDAO;

	@Autowired
	private BatchValidator batchValidator;

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

		this.batchValidator.validateWaste(drugOrder, location, quantity, date);
		final List<Batch> batches = this.batchDAO.findByDrugAndLocationAndNotExpiredDate(drugOrder.getDrug(), location,
				date, false);

		if (batches.isEmpty()) {

			throw new APIException(Context.getMessageSourceService().getMessage("inventorypoc.error.noStockAvailablity",
					new String[] { drugOrder.getDrug().getDisplayName() }, Context.getLocale()));
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
				throw new APIException(
						Context.getMessageSourceService().getMessage("inventorypoc.error.insuficientStock.availability",
								new String[] { drugOrder.getDrug().getDisplayName() }, Context.getLocale()));
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

		this.batchValidator.validateAdjustments(batch, newRemainPackageQuantityUnits);

		Double deltaQuantity = newRemainPackageQuantityUnits - batch.getRemainPackageQuantityUnits();
		batch.setRemainPackageQuantityUnits(batch.getRemainPackageQuantityUnits() + deltaQuantity);

		BatchOperationType batchOperationType = BatchOperationType.POSETIVE_ADJUSTMENT;
		if (deltaQuantity < 0) {
			batchOperationType = BatchOperationType.NEGATIVE_ADJUSTMENT;
			deltaQuantity = deltaQuantity * -1;
		}

		final BatchEntry batchEntry = new BatchEntry(batch, batchOperationType, deltaQuantity);
		batch.setRemainPackageQuantityUnits(newRemainPackageQuantityUnits);

		this.batchEntryDAO.save(batchEntry);
		this.batchDAO.save(batch);

		return batch;
	}

	@Override
	public Batch adjustBatchWithDeliverNoteItem(final Batch batch, final DeliverNoteItem deliverNoteItem) {

		final Double newQuantity = deliverNoteItem.getQuantity() * deliverNoteItem.getDrugPackage().getTotalQuantity();

		batch.setPackageQuantity(batch.getPackageQuantity() + deliverNoteItem.getQuantity());
		batch.setPackageQuantityUnits(batch.getPackageQuantityUnits() + newQuantity);
		batch.setRemainPackageQuantityUnits(batch.getRemainPackageQuantityUnits() + newQuantity);

		this.batchDAO.save(batch);
		final BatchEntry batchEntry = new BatchEntry(batch, BatchOperationType.RECEIPT, newQuantity);
		this.batchEntryDAO.save(batchEntry);

		return batch;
	}

	@Override
	public Batch findBatchById(final Integer batchId) {
		return this.batchDAO.findById(batchId);
	}

	@Override
	public Double findCurrentStockBalance(final Drug drug, final Location location, final Date date) {

		final List<Batch> batches = this.findBatchesByDrugAndLocationAndNotExpiredDate(drug, location, date);

		Double currentBalance = 0d;

		for (final Batch batch : batches) {
			currentBalance = currentBalance + batch.getRemainPackageQuantityUnits();
		}

		return currentBalance;
	}

}
