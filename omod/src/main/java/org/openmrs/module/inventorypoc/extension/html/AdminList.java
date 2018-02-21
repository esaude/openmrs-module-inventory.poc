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
package org.openmrs.module.inventorypoc.extension.html;

import java.util.LinkedHashMap;
import java.util.Map;

import org.openmrs.module.Extension;
import org.openmrs.module.web.extension.AdministrationSectionExt;

/**
 * This class defines the links that will appear on the administration page
 * under the "inventorypoc.title" heading.
 */
public class AdminList extends AdministrationSectionExt {

	/**
	 * @see AdministrationSectionExt#getMediaType()
	 */
	@Override
	public Extension.MEDIA_TYPE getMediaType() {
		return Extension.MEDIA_TYPE.html;
	}

	/**
	 * @see AdministrationSectionExt#getTitle()
	 */
	@Override
	public String getTitle() {
		return "inventorypoc.title";
	}

	/**
	 * @see AdministrationSectionExt#getLinks()
	 */
	@Override
	public Map<String, String> getLinks() {
		final LinkedHashMap<String, String> map = new LinkedHashMap<>();
		map.put("module/inventorypoc/deliverNote/upload.form", "inventorypoc.import.deliver.note");
		map.put("module/inventorypoc/stockAdjustment/initForm.form", "inventorypoc.stockAdjustment");

		return map;
	}

}
