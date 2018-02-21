package org.openmrs.module.inventorypoc.batch.dao;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.hibernate.criterion.Restrictions;
import org.openmrs.EncounterType;
import org.openmrs.Order;
import org.openmrs.module.inventorypoc.batch.model.Batch;
import org.openmrs.module.inventorypoc.batch.model.BatchEntry;
import org.openmrs.module.inventorypoc.batch.model.BatchEntry.BatchOperationType;

public class BatchEntryDAOImpl implements BatchEntryDAO {
	
	private SessionFactory sessionFactory;
	
	private BatchDAO batchDAO;
	
	@Override
	public void setSessionFactory(final SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	@Override
	public BatchEntry save(final BatchEntry batchEntry) {
		this.sessionFactory.getCurrentSession().save(batchEntry);
		return batchEntry;
	}
	
	@Override
	public void reverse(final BatchEntry batchEntry) {
		final Session currentSession = this.sessionFactory.getCurrentSession();
		final Criteria searchCriteria = currentSession.createCriteria(BatchEntry.class, "batchEntry");
		searchCriteria.createAlias("batchEntry.batch", "batch");
		searchCriteria.add(Restrictions.eq("batchEntry.batchEntryId", batchEntry.getId()));
		final BatchEntry toBeReversed = (BatchEntry) searchCriteria.uniqueResult();
		
		final Batch batch = toBeReversed.getBatch();
		batch.setRemainPackageQuantityUnits(batch.getRemainPackageQuantityUnits() + toBeReversed.getQuantity());
		final BatchEntry reversalRequestor = this.prepareReversalRequestor(toBeReversed);
		
		this.batchDAO.updateBatch(batch);
		this.save(reversalRequestor);
		toBeReversed.setReversalRequestor(reversalRequestor);
		this.sessionFactory.getCurrentSession().update(toBeReversed);
	}
	
	@Override
	public void retire(final BatchEntry batchEntry) {
	}
	
	private BatchEntry prepareReversalRequestor(final BatchEntry reversed) {
		final BatchEntry reversalRequestor = new BatchEntry();
		
		reversalRequestor.setBatch(reversed.getBatch());
		reversalRequestor.setReversed(reversed);
		reversalRequestor.setQuantity(reversed.getQuantity());
		reversalRequestor.setBatchOperationType(reversed.getBatchOperationType());
		reversalRequestor.setOrder(reversed.getOrder());
		return reversalRequestor;
	}
	
	@Override
	public void setBatchDAO(final BatchDAO batchDAO) {
		this.batchDAO = batchDAO;
	}
	
	@Override
	public BatchEntry create(final Order order, final Batch batch, final BatchOperationType operationType,
	        final Double quantity) {
		
		final BatchEntry entry = new BatchEntry();
		entry.setBatch(batch);
		entry.setBatchOperationType(operationType);
		entry.setQuantity(quantity);
		entry.setOrder(order);
		this.save(entry);
		
		return entry;
	}
	
	@Override
	public BatchEntry findByDispensedOrder(final Order order, final EncounterType encounterType,
	        final boolean retired) {
		
		final Session currentSession = this.sessionFactory.getCurrentSession();
		final Criteria searchCriteria = currentSession.createCriteria(BatchEntry.class, "batchEntry");
		searchCriteria.add(Restrictions.eq("batchEntry.order", order));
		searchCriteria.add(Restrictions.eq("batchEntry.retired", retired));
		searchCriteria.createAlias("batchEntry.batch", "batch");
		searchCriteria.add(Restrictions.eq("batch.retired", retired));
		searchCriteria.createAlias("batchEntry.order", "order");
		searchCriteria.createAlias("order.encounter", "encounter");
		searchCriteria.add(Restrictions.eq("encounter.encounterType", encounterType));
		
		return (BatchEntry) searchCriteria.uniqueResult();
	}
}
