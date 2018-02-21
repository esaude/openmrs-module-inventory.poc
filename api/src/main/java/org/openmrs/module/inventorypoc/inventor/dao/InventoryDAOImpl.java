package org.openmrs.module.inventorypoc.inventor.dao;

import org.hibernate.SessionFactory;
import org.openmrs.module.inventorypoc.inventor.model.Inventory;

public class InventoryDAOImpl implements InventoryDAO {
	
	private SessionFactory sessionFactory;
	
	@Override
	public void setSessionFactory(final SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	@Override
	public Inventory save(final Inventory inventory) {
		this.sessionFactory.getCurrentSession().save(inventory);
		return inventory;
	}
}
