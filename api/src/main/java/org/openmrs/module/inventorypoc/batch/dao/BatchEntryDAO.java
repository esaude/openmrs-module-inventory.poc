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
