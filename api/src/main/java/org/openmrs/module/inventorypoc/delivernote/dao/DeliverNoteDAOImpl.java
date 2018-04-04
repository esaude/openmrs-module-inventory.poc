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

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.openmrs.module.inventorypoc.common.util.DateUtils;
import org.openmrs.module.inventorypoc.delivernote.model.DeliverNote;

public class DeliverNoteDAOImpl implements DeliverNoteDAO {
	
	protected final Log log = LogFactory.getLog(this.getClass());
	
	private SessionFactory sessionFactory;
	
	@Override
	public void setSessionFactory(final SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	@Override
	public DeliverNote save(final DeliverNote deliverNote) {
		this.sessionFactory.getCurrentSession().saveOrUpdate(deliverNote);
		return deliverNote;
	}
	
	@Override
	public DeliverNote findBySimamNumberAndDeliveryDate(final String simamNumber, final Date deliveryDate,
	        final boolean retired) {
		
		final Criteria criteria = this.sessionFactory.getCurrentSession().createCriteria(DeliverNote.class,
		    "deliverNote");
		criteria.add(Restrictions.eq("deliverNote.simamNumber", simamNumber));
		criteria.add(Restrictions.ge("deliverNote.deliveryDate", DateUtils.lowDateTime(deliveryDate)));
		criteria.add(Restrictions.le("deliverNote.deliveryDate", DateUtils.highDateTime(deliveryDate)));
		criteria.add(Restrictions.eq("deliverNote.retired", retired));
		
		return (DeliverNote) criteria.uniqueResult();
	}
}
