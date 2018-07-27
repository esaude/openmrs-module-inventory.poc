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
package org.openmrs.module.inventorypoc.drugpackage.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.openmrs.Drug;
import org.openmrs.module.inventorypoc.drugpackage.model.DrugPackage;

public class DrugPackageDAOImpl implements DrugPackageDAO {
	
	protected final Log log = LogFactory.getLog(this.getClass());
	
	private SessionFactory sessionFactory;
	
	@Override
	public void setSessionFactory(final SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	@Override
	public DrugPackage save(final DrugPackage drugPackage) {
		this.sessionFactory.getCurrentSession().saveOrUpdate(drugPackage);
		return drugPackage;
	}
	
	@Override
	public DrugPackage findById(final Integer id) {
		
		final Criteria searchCriteria = this.sessionFactory.getCurrentSession()
		        .createCriteria(DrugPackage.class, "drugPackage").add(Restrictions.eq("drugPackage.drugPackageId", id));
		return (DrugPackage) searchCriteria.uniqueResult();
	}
	
	@Override
	public DrugPackage findByDrug(final Drug drug) {
		
		final Criteria searchCriteria = this.sessionFactory.getCurrentSession().createCriteria(DrugPackage.class,
		    "drugPackage");
		searchCriteria.add(Restrictions.eq("drugPackage.drug", drug));
		
		return (DrugPackage) searchCriteria.uniqueResult();
	}
	
	@Override
	public Drug findDrugByFNMCode(final String drugFnmCode) {
		final Query query = this.sessionFactory.getCurrentSession()
		        .createSQLQuery(
		            "select d.* from phm_drug_items di, drug d where di.drug_id = d.drug_id  and di.fnm_code = :fnmCode")
		        .addEntity(Drug.class).setParameter("fnmCode", drugFnmCode);
		
		return (Drug) query.uniqueResult();
		
	}
}
