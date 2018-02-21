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
