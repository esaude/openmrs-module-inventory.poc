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
package org.openmrs.module.inventorypoc.web.validator;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.openmrs.module.inventorypoc.web.bean.UploadFile;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.multipart.MultipartFile;

@Component
public class UploadFileValidator implements Validator {

	private static final String XLS_FILE_EXTENSION = "xls";

	/**
	 * @see org.springframework.validation.Validator#supports(java.lang.Class)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public boolean supports(final Class clazz) {
		return UploadFile.class.isAssignableFrom(clazz);
	}

	/**
	 * @see org.springframework.validation.Validator#validate(java.lang.Object,
	 *      org.springframework.validation.Errors)
	 */
	@Override
	public void validate(final Object obj, final Errors errors) {
		final UploadFile uploadPackage = (UploadFile) obj;
		final MultipartFile multipartFile = uploadPackage.getFile();

		if ((multipartFile == null) || multipartFile.isEmpty()) {
			errors.rejectValue("file", "inventorypoc.error.file.empty");
			return;
		}
		this.checkFileExtension(multipartFile, errors);
	}

	private void checkFileExtension(final MultipartFile multipartFile, final Errors errors) {

		final File file = new File(multipartFile.getName());
		try {
			multipartFile.transferTo(file);

			final String fileExtension = FilenameUtils.getExtension(multipartFile.getOriginalFilename());

			if (!UploadFileValidator.XLS_FILE_EXTENSION.equals(StringUtils.lowerCase(fileExtension))) {
				errors.rejectValue("file", "inventorypoc.error.wrong.file.extension");
				return;
			}

			multipartFile.transferTo(file);

		} catch (IllegalStateException | IOException e) {

			errors.rejectValue("file", "inventorypoc.error.wrong.file.general", new String[] { e.getMessage() }, null);
		}
	}
}
