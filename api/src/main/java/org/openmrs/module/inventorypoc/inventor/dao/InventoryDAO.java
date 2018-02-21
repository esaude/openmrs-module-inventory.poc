package org.openmrs.module.inventorypoc.inventor.dao;

import org.hibernate.SessionFactory;
import org.openmrs.module.inventorypoc.inventor.model.Inventory;

public interface InventoryDAO {
	
	public void setSessionFactory(SessionFactory sessionFactory);
	
	Inventory save(Inventory inventory);
}
