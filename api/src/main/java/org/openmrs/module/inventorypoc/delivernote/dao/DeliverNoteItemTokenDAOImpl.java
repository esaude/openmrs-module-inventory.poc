package org.openmrs.module.inventorypoc.delivernote.dao;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.openmrs.module.inventorypoc.delivernote.model.DeliverNoteItemToken;

public class DeliverNoteItemTokenDAOImpl implements DeliverNoteItemTokenDAO {
	
	private SessionFactory sessionFactory;
	
	@Override
	public void setSessionFactory(final SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
		
	}
	
	@Override
	public DeliverNoteItemToken save(final DeliverNoteItemToken deliverNoteItemToken) {
		this.sessionFactory.getCurrentSession().save(deliverNoteItemToken);
		return deliverNoteItemToken;
	}
	
	@Override
	public DeliverNoteItemToken findByTokenNumber(final String tokenNumber, final boolean retired) {
		
		final Criteria criteria = this.sessionFactory.getCurrentSession().createCriteria(DeliverNoteItemToken.class,
		    "token");
		criteria.add(Restrictions.eq("token.tokenNumber", tokenNumber));
		criteria.add(Restrictions.eq("token.retired", retired));
		
		return (DeliverNoteItemToken) criteria.uniqueResult();
	}
}
