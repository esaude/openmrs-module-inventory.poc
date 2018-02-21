package org.openmrs.module.inventorypoc.delivernote.dao;

import org.hibernate.SessionFactory;
import org.openmrs.module.inventorypoc.delivernote.model.DeliverNoteItem;
import org.openmrs.module.inventorypoc.drugpackage.model.DrugPackage;

public interface DeliverNoteItemDAO {
	
	public void setSessionFactory(SessionFactory sessionFactory);
	
	public DeliverNoteItem save(DeliverNoteItem deliverNoteItem);
	
	public DeliverNoteItem findByOriginDocumentAndSimamNumberAndDrugPackage(final String originDocument,
	        final String simamNumber, final DrugPackage drugPackage);
	
}
