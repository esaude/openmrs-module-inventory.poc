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

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.openmrs.Drug;
import org.openmrs.Location;
import org.openmrs.module.inventorypoc.batch.model.Batch;
import org.openmrs.module.inventorypoc.common.util.DateUtils;

public class BatchDAOImpl implements BatchDAO {
	
	private SessionFactory sessionFactory;
	
	@Override
	public void setSessionFactory(final SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	@Override
	public Batch save(final Batch batch) {
		// TODO save or update does not working quite well for update.
		// it is strange
		if (batch.getId() == null) {
			this.sessionFactory.getCurrentSession().save(batch);
		} else {
			this.sessionFactory.getCurrentSession().update(batch);
		}
		return batch;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Batch> findByDrugAndLocationAndNotExpiredDate(final Drug drug, final Location location, final Date date,
	        final boolean retired) {
		
		final Criteria searchCriteria = this.sessionFactory.getCurrentSession().createCriteria(Batch.class, "batch");
		searchCriteria.createAlias("batch.drugPackage", "drugPackage");
		searchCriteria.add(Restrictions.eq("drugPackage.drug", drug));
		searchCriteria.add(Restrictions.eq("batch.location", location));
		searchCriteria.add(Restrictions.eq("batch.retired", retired));
		searchCriteria.add(Restrictions.gt("batch.remainPackageQuantityUnits", Double.valueOf(0)));
		searchCriteria.add(Restrictions.gt("batch.expireDate", DateUtils.highDateTime(date)));
		searchCriteria.addOrder(Order.asc("batch.expireDate"));
		
		return searchCriteria.list();
	}
	
	@Override
	public void updateBatches(final List<Batch> batchs) {
		for (final Batch batch : batchs) {
			this.sessionFactory.getCurrentSession().update(batch);
		}
	}
	
	@Override
	public void updateBatch(final Batch batch) {
		this.sessionFactory.getCurrentSession().update(batch);
	}
	
	@Override
	public void decreaseRemainPackageQuantityUnits(final Batch batch, final Double quantity) {
		batch.setRemainPackageQuantityUnits(batch.getRemainPackageQuantityUnits() - quantity);
		this.sessionFactory.getCurrentSession().update(batch);
	}
	
	@Override
	public List<Batch> findByLocationAndAvailableQuantity(final Location location, final Date currentDate,
	        final boolean retired) {
		
		final Criteria searchCriteria = this.sessionFactory.getCurrentSession().createCriteria(Batch.class, "batch");
		searchCriteria.setFetchMode("batch.drugPackage", FetchMode.JOIN);
		searchCriteria.setFetchMode("batch.drugPackage.drug", FetchMode.JOIN);
		searchCriteria.setFetchMode("batch.location", FetchMode.JOIN);
		searchCriteria.add(Restrictions.eq("location", location));
		searchCriteria.add(Restrictions.eq("batch.retired", retired));
		searchCriteria.add(Restrictions.gt("batch.remainPackageQuantityUnits", Double.valueOf(0)));
		searchCriteria.add(Restrictions.gt("batch.expireDate", DateUtils.highDateTime(currentDate)));
		searchCriteria.addOrder(Order.asc("batch.expireDate"));
		
		return searchCriteria.list();
	}
	
	@Override
	public Batch findById(final Integer batchId) {
		
		final Criteria searchCriteria = this.sessionFactory.getCurrentSession().createCriteria(Batch.class, "batch");
		searchCriteria.add(Restrictions.eq("batch.batchId", batchId));
		
		return (Batch) searchCriteria.uniqueResult();
	}
}
