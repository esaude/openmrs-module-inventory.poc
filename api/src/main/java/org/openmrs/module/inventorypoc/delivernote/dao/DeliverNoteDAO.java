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

import org.hibernate.SessionFactory;
import org.openmrs.module.inventorypoc.delivernote.model.DeliverNote;

public interface DeliverNoteDAO {
	
	public void setSessionFactory(SessionFactory sessionFactory);
	
	public DeliverNote save(DeliverNote deliverNote);
	
	public DeliverNote findBySimamNumberAndDeliveryDate(final String simamNumber, final Date deliveryDate,
	        boolean retired);
	
}
