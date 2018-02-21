package org.openmrs.module.inventorypoc.drugpackage.service;

import org.openmrs.Drug;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.inventorypoc.drugpackage.dao.DrugPackageDAO;
import org.openmrs.module.inventorypoc.drugpackage.model.DrugPackage;

/**
 *
 */
public class DrugPackageServiceImpl extends BaseOpenmrsService implements DrugPackageService {
	
	private DrugPackageDAO drugPackageDAO;
	
	@Override
	public void setDrugPackageDAO(final DrugPackageDAO drugPackageDAO) {
		this.drugPackageDAO = drugPackageDAO;
	}
	
	@Override
	public DrugPackage saveDrugPackage(final DrugPackage drugPackage) {
		return this.drugPackageDAO.save(drugPackage);
	}
	
	@Override
	public DrugPackage fingDrugPackageById(final Integer id) {
		return this.drugPackageDAO.findById(id);
	}
	
	@Override
	public DrugPackage findDrugPackageByDrugAndTotalQuantity(final Drug drug, final Double totalQuantity) {
		
		return this.drugPackageDAO.findByDrugAndTotalQuantity(drug, totalQuantity);
	}
	
	@Override
	public Drug findDrugByDrugFNMCode(final String fnmCode) {
		return this.drugPackageDAO.findDrugByFNMCode(fnmCode);
	}
}
