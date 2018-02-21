package org.openmrs.module.inventorypoc.drugpackage.service;

import org.openmrs.Drug;
import org.openmrs.api.OpenmrsService;
import org.openmrs.module.inventorypoc.drugpackage.dao.DrugPackageDAO;
import org.openmrs.module.inventorypoc.drugpackage.model.DrugPackage;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface DrugPackageService extends OpenmrsService {
	
	public void setDrugPackageDAO(DrugPackageDAO drugPackageDAO);
	
	public DrugPackage saveDrugPackage(DrugPackage drugPackage);
	
	public DrugPackage fingDrugPackageById(Integer id);
	
	public DrugPackage findDrugPackageByDrugAndTotalQuantity(final Drug drug, Double totalQuantity);
	
	public Drug findDrugByDrugFNMCode(String fnmCode);
}
