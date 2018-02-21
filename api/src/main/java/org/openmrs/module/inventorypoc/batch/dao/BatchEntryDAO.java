package org.openmrs.module.inventorypoc.batch.dao;

import org.hibernate.SessionFactory;
import org.openmrs.EncounterType;
import org.openmrs.Order;
import org.openmrs.module.inventorypoc.batch.model.Batch;
import org.openmrs.module.inventorypoc.batch.model.BatchEntry;
import org.openmrs.module.inventorypoc.batch.model.BatchEntry.BatchOperationType;

public interface BatchEntryDAO {

	public void setSessionFactory(SessionFactory sessionFactory);

	public void setBatchDAO(BatchDAO batchDAO);

	public BatchEntry create(Order order, Batch batch, BatchOperationType operationType, Double quantity);

	public BatchEntry save(BatchEntry batchEntry);

	public void reverse(BatchEntry batchEntry);

	public void retire(BatchEntry batchEntry);

	public BatchEntry findByDispensedOrder(Order order, EncounterType encounterType, boolean retired);

}
