package org.openmrs.module.inventorypoc.delivernote.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
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
	public DeliverNote findByOriginDocumentAndSimamNumber(final String originDocument, final String simamNumber) {
		
		final Criteria criteria = this.sessionFactory.getCurrentSession().createCriteria(DeliverNote.class,
		    "deliverNote");
		criteria.add(Restrictions.eq("deliverNote.originDocumentCode", originDocument));
		criteria.add(Restrictions.eq("deliverNote.simamNumber", simamNumber));
		
		return (DeliverNote) criteria.uniqueResult();
	}
}
