package org.openmrs.module.inventorypoc.drugpackage.dao;

import org.hibernate.SessionFactory;
import org.openmrs.Drug;
import org.openmrs.module.inventorypoc.drugpackage.model.DrugPackage;

public interface DrugPackageDAO {
	
	public void setSessionFactory(SessionFactory sessionFactory);
	
	public DrugPackage save(DrugPackage drugPackage);
	
	public DrugPackage findById(Integer id);
	
	public DrugPackage findByDrugAndTotalQuantity(Drug drug, Double totalQuantity);
	
	public Drug findDrugByFNMCode(String fnmCode);
	
}
