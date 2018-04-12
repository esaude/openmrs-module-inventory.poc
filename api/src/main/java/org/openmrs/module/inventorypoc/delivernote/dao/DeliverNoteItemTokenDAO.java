package org.openmrs.module.inventorypoc.delivernote.dao;

import org.hibernate.SessionFactory;
import org.openmrs.module.inventorypoc.delivernote.model.DeliverNoteItemToken;

public interface DeliverNoteItemTokenDAO {
	
	void setSessionFactory(final SessionFactory sessionFactory);
	
	DeliverNoteItemToken save(DeliverNoteItemToken deliverNoteItemToken);
	
	DeliverNoteItemToken findByTokenNumber(String tokenNumber, boolean retired);
}
