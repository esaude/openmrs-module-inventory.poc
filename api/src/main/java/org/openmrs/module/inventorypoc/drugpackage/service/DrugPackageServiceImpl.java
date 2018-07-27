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
	public DrugPackage findDrugPackageByDrug(final Drug drug) {
		
		return this.drugPackageDAO.findByDrug(drug);
	}
	
	@Override
	public Drug findDrugByDrugFNMCode(final String fnmCode) {
		return this.drugPackageDAO.findDrugByFNMCode(fnmCode);
	}
}
