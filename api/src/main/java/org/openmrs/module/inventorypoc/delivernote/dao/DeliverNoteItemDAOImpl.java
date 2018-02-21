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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
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
	public DeliverNoteItem findByOriginDocumentAndSimamNumberAndDrugPackage(final String originDocument,
	        final String simamNumber, final DrugPackage drugPackage) {
		
		final Criteria criteria = this.sessionFactory.getCurrentSession().createCriteria(DeliverNoteItem.class, "item");
		
		criteria.add(Restrictions.eq("item.originDocumentCode", originDocument));
		criteria.add(Restrictions.eq("item.simamNumber", simamNumber));
		criteria.add(Restrictions.eq("item.drugPackage", drugPackage));
		
		return (DeliverNoteItem) criteria.uniqueResult();
	}
}
