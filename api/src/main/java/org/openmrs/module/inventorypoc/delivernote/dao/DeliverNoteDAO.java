package org.openmrs.module.inventorypoc.delivernote.dao;

import org.hibernate.SessionFactory;
import org.openmrs.module.inventorypoc.delivernote.model.DeliverNote;

public interface DeliverNoteDAO {
	
	public void setSessionFactory(SessionFactory sessionFactory);
	
	public DeliverNote save(DeliverNote deliverNote);
	
	public DeliverNote findByOriginDocumentAndSimamNumber(String originDocument, String SIMAMDocument);
	
}
