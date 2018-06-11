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
package org.openmrs.module.inventorypoc.delivernote.dao;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.openmrs.Drug;
import org.openmrs.module.inventorypoc.batch.model.Batch;
import org.openmrs.module.inventorypoc.delivernote.model.DeliverNoteItem;
import org.openmrs.module.inventorypoc.drugpackage.model.DrugPackage;

public class DeliverNoteItemDAOImpl implements DeliverNoteItemDAO {
	
	protected final Log log = LogFactory.getLog(this.getClass());
	
	private SessionFactory sessionFactory;
	
	@Override
	public void setSessionFactory(final SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	@Override
	public DeliverNoteItem save(final DeliverNoteItem deliverNoteItem) {
		this.sessionFactory.getCurrentSession().save(deliverNoteItem);
		return deliverNoteItem;
	}
	
	@Override
	public DeliverNoteItem findByTokenNumber(final String tokenNumber, final boolean retired) {
		
		final Criteria criteria = this.sessionFactory.getCurrentSession().createCriteria(DeliverNoteItem.class, "item");
		criteria.add(Restrictions.eq("item.tokenNumber", tokenNumber));
		criteria.add(Restrictions.eq("item.retired", retired));
		
		return (DeliverNoteItem) criteria.uniqueResult();
	}
	
	@Override
	public DeliverNoteItem findByLotAndTokenNumber(final String lotNumber, final String tokenNumber,
	        final boolean retired) {
		
		final Criteria criteria = this.sessionFactory.getCurrentSession().createCriteria(DeliverNoteItem.class, "item");
		criteria.add(Restrictions.eq("item.lotNumber", lotNumber));
		criteria.add(Restrictions.eq("item.tokenNumber", tokenNumber));
		criteria.add(Restrictions.eq("item.retired", retired));
		
		return (DeliverNoteItem) criteria.uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public DeliverNoteItem findByDrugAndLotNumber(final Drug drug, final String loteNumber, final boolean retired) {
		
		final Criteria searchCriteria = this.sessionFactory.getCurrentSession().createCriteria(DeliverNoteItem.class,
		    "item");
		searchCriteria.createAlias("item.drugPackage", "package");
		searchCriteria.add(Restrictions.eq("package.drug", drug));
		searchCriteria.add(Restrictions.eq("item.lotNumber", loteNumber));
		searchCriteria.add(Restrictions.eq("item.retired", retired));
		final List<DeliverNoteItem> list = searchCriteria.list();
		
		if (!list.isEmpty()) {
			return list.iterator().next();
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<DeliverNoteItem> findByBatchAndDrugPackage(final Batch batch, final DrugPackage drugPackage,
	        final boolean retired) {
		final Criteria searchCriteria = this.sessionFactory.getCurrentSession().createCriteria(DeliverNoteItem.class,
		    "item");
		searchCriteria.add(Restrictions.eq("item.batch", batch));
		searchCriteria.add(Restrictions.eq("item.drugPackage", drugPackage));
		searchCriteria.add(Restrictions.eq("item.retired", retired));
		
		return searchCriteria.list();
	}
}
