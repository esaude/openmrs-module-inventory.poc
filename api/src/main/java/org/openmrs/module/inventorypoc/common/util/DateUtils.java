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
package org.openmrs.module.inventorypoc.common.util;

import java.util.Calendar;
import java.util.Date;

public class DateUtils {
	
	/**
	 * Retorna o valor do horário minimo para a data de referencia passada. <BR>
	 * <BR>
	 * Por exemplo se a data for "30/01/2009 as 17h:33m:12s e 299ms" a data retornada por este
	 * metodo será "30/01/2009 as 00h:00m:00s e 000ms".
	 * 
	 * @param date de referencia.
	 * @return {@link Date} que representa o horário minimo para dia informado.
	 */
	public static Date lowDateTime(final Date date) {
		final Calendar aux = Calendar.getInstance();
		aux.setTime(date);
		DateUtils.toOnlyDate(aux); // zera os parametros de hour,min,sec,milisec
		return aux.getTime();
	}
	
	/**
	 * Retorna o valor do horário maximo para a data de referencia passada. <BR>
	 * <BR>
	 * Por exemplo se a data for "30/01/2009 as 17h:33m:12s e 299ms" a data retornada por este
	 * metodo será "30/01/2009 as 23h:59m:59s e 999ms".
	 * 
	 * @param date de referencia.
	 * @return {@link Date} que representa o horário maximo para dia informado.
	 */
	public static Date highDateTime(final Date date) {
		
		final Calendar aux = Calendar.getInstance();
		aux.setTime(date);
		aux.set(Calendar.HOUR_OF_DAY, 23);
		aux.set(Calendar.MINUTE, 59);
		aux.set(Calendar.SECOND, 59);
		aux.set(Calendar.MILLISECOND, 999);
		return aux.getTime();
	}
	
	/**
	 * Zera todas as referencias de hora, minuto, segundo e milesegundo do {@link Calendar}.
	 * 
	 * @param date a ser modificado.
	 */
	public static void toOnlyDate(final Calendar date) {
		date.set(Calendar.HOUR_OF_DAY, 0);
		date.set(Calendar.MINUTE, 0);
		date.set(Calendar.SECOND, 0);
		date.set(Calendar.MILLISECOND, 0);
	}
}
