/**
 *
 */
package org.openmrs.module.inventorypoc.web.transformer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.Normalizer;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.openmrs.module.inventorypoc.web.bean.DeliverNoteItemRow;
import org.openmrs.module.inventorypoc.web.bean.DeliverNoteRow;
import org.springframework.stereotype.Component;

@Component
public class XlsToPojoObjectTransformer {
	
	public DeliverNoteRow toDeliverNote(final File file) throws IOException {
		
		DeliverNoteRow deliverNoteRow = null;
		if (file.isFile() && file.exists()) {
			
			final FileInputStream fileInputStream = new FileInputStream(file);
			final HSSFWorkbook workbook = new HSSFWorkbook(fileInputStream);
			
			final HSSFSheet sheet = workbook.getSheetAt(0);
			
			deliverNoteRow = this.generateDeliverNoteRow(sheet);
			
			fileInputStream.close();
			workbook.close();
		}
		return deliverNoteRow;
	}
	
	private DeliverNoteRow generateDeliverNoteRow(final HSSFSheet sheet) {
		final DeliverNoteRow deliverNoteRow = new DeliverNoteRow(this.getCellStringValue(sheet.getRow(4), 2),
		        this.getCellStringValue(sheet.getRow(5), 2), this.getCellStringValue(sheet.getRow(6), 2));
		
		for (int i = 10; i <= sheet.getLastRowNum(); i++) {
			
			final HSSFRow row = sheet.getRow(i);
			int rowCount = -1;
			
			final DeliverNoteItemRow itemRow = new DeliverNoteItemRow(this.getCellStringValue(row, ++rowCount),
			        this.getCellStringValue(row, ++rowCount), this.getCellStringValue(row, ++rowCount),
			        this.getCellStringValue(row, ++rowCount), this.getCellStringValue(row, ++rowCount),
			        this.getCellStringValue(row, ++rowCount), this.getCellStringValue(row, ++rowCount),
			        this.getCellStringValue(row, ++rowCount), this.getCellStringValue(row, ++rowCount),
			        this.getCellStringValue(row, ++rowCount));
			deliverNoteRow.addDeliverNoteItemRow(itemRow);
		}
		return deliverNoteRow;
	}
	
	private String getCellStringValue(final HSSFRow row, final int cellNumber) {
		try {
			return this.normalize(row.getCell(cellNumber).toString());
		}
		catch (final Exception e) {
			return StringUtils.EMPTY;
		}
	}
	
	private String normalize(final String toNormalize) {
		return Normalizer.normalize(toNormalize, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "").toUpperCase()
		        .trim();
	}
	
	public static void main(final String[] args) {
		
		final String pattern = "([0-3]{1}[0-9]{1})/([0-1]{1}[0-9]{1})/([1-3]{1}[0-9]{3})";
		
		System.out.println("39/11/3016".matches(pattern));
	}
}
