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

import java.util.Date;
import java.util.List;

import org.hibernate.SessionFactory;
import org.openmrs.Drug;
import org.openmrs.Location;
import org.openmrs.module.inventorypoc.batch.model.Batch;

public interface BatchDAO {
	
	public void setSessionFactory(SessionFactory sessionFactory);
	
	public Batch save(Batch batch);
	
	public void updateBatch(Batch batch);
	
	public void decreaseRemainPackageQuantityUnits(Batch batch, Double quantity);
	
	public void updateBatches(List<Batch> batchs);
	
	public List<Batch> findByDrugAndLocationAndNotExpiredDate(Drug drug, Location location, Date date, boolean retired);
	
	public List<Batch> findByLocationAndAvailableQuantity(Location location, Date currentDate, boolean retired);
	
	Batch findById(Integer batchId);
	
}
