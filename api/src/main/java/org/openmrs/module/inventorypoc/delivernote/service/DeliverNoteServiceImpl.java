/**
 *
 */
package org.openmrs.module.inventorypoc.delivernote.service;

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
import org.openmrs.module.inventorypoc.drugpackage.model.DrugPackage;
import org.openmrs.module.inventorypoc.drugpackage.service.DrugPackageService;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class DeliverNoteServiceImpl extends BaseOpenmrsService implements DeliverNoteService {

	private DeliverNoteDAO deliverNoteDAO;

	private DeliverNoteItemDAO deliverNoteItemDAO;

	private BatchService batchService;

	private DrugPackageService drugPackageService;

	private LocationService locationService;

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

		final Location location = this.locationService.getLocation(deliverNote.getLocation().getLocationId());
		DeliverNote foundDeliverNote = this.deliverNoteDAO
				.findByOriginDocumentAndSimamNumber(deliverNote.getOriginDocumentCode(), deliverNote.getSimamNumber());

		if (foundDeliverNote == null) {
			deliverNote.setLocation(location);
			deliverNote.setUuid(UUID.randomUUID().toString());
			foundDeliverNote = this.deliverNoteDAO.save(deliverNote);
		}

		for (final DeliverNoteItem noteItem : deliverNote.getDeliverNoteItems()) {
			final DrugPackage drugPackage = this.drugPackageService
					.fingDrugPackageById(noteItem.getDrugPackage().getId());

			final Batch batch = new Batch();
			batch.setReciptDate(foundDeliverNote.getReciptDate());
			batch.setExpireDate(noteItem.getExpireDate());
			batch.setDrugPackage(noteItem.getDrugPackage());
			batch.setPackageQuantity(noteItem.getQuantity());
			batch.setPackageQuantityUnits(noteItem.getQuantity() * drugPackage.getTotalQuantity());
			batch.setLocation(location);
			batch.setRemainPackageQuantityUnits(batch.getPackageQuantityUnits());
			batch.setUnBalancedUnitsQuantity(0d);
			batch.setVersion(1d);

			final DeliverNoteItem deliverNoteItem = new DeliverNoteItem();
			deliverNoteItem.setBatch(batch);
			deliverNoteItem.setDeliverNote(foundDeliverNote);
			deliverNoteItem.setExpireDate(noteItem.getExpireDate());
			deliverNoteItem.setQuantity(noteItem.getQuantity());
			deliverNoteItem.setDrugPackage(drugPackage);
			deliverNoteItem.setUnitPrice(noteItem.getUnitPrice());
			deliverNoteItem.setUuid(UUID.randomUUID().toString());
			deliverNoteItem.setAuthorizedQuantity(noteItem.getAuthorizedQuantity());
			deliverNoteItem.setSimamNumber(noteItem.getSimamNumber());
			deliverNoteItem.setOriginDocumentCode(noteItem.getOriginDocumentCode());
			deliverNoteItem.setRequestedQuantity(noteItem.getRequestedQuantity());

			this.batchService.createBatch(batch, BatchOperationType.RECEIPT);
			this.deliverNoteItemDAO.save(deliverNoteItem);
		}
		return foundDeliverNote;
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
	public DeliverNote findDeliverNoteByOriginDocumentAndSimamNumber(final String originDocument,
			final String simamNumber) {
		return this.deliverNoteDAO.findByOriginDocumentAndSimamNumber(originDocument, simamNumber);
	}

	@Override
	public DeliverNoteItem findDeliverNoteItemByOriginDocumentAndSimamNumberAndDrugPackage(final String originDocument,
			final String simamNumber, final String drugFnmCode, final Double totalQuantityPerPackage) {

		final Drug drug = this.drugPackageService.findDrugByDrugFNMCode(drugFnmCode);
		final DrugPackage drugPackage = this.drugPackageService.findDrugPackageByDrugAndTotalQuantity(drug,
				totalQuantityPerPackage);

		return this.deliverNoteItemDAO.findByOriginDocumentAndSimamNumberAndDrugPackage(originDocument, simamNumber,
				drugPackage);
	}
}
