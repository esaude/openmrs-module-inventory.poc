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
package org.openmrs.module.inventorypoc.drugpackage.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.openmrs.Drug;
import org.openmrs.module.inventorypoc.common.model.BaseOpenmrsMetadataWrapper;

@Entity
@Table(name = "pocinv_drug_package")
public class DrugPackage extends BaseOpenmrsMetadataWrapper implements Comparable<DrugPackage> {
	
	/**
	 *
	 */
	private static final long serialVersionUID = 3219164603652473482L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "drug_package_id")
	private Integer drugPackageId;
	
	@ManyToOne
	@JoinColumn(name = "drug_id")
	private Drug drug;
	
	@Column(name = "barcode")
	private String barcode;
	
	@Column(name = "total_quantity")
	private Double totalQuantity;
	
	@Override
	public Integer getId() {
		return this.drugPackageId;
	}
	
	public DrugPackage() {
	}
	
	public DrugPackage(final String barCode, final Double totalQuantity) {
		this.barcode = barCode;
		this.totalQuantity = totalQuantity;
	}
	
	public DrugPackage(final Integer drugPackageId) {
		this.drugPackageId = drugPackageId;
	}
	
	@Override
	public void setId(final Integer drugPackageId) {
		
		this.drugPackageId = drugPackageId;
	}
	
	public Drug getDrug() {
		return this.drug;
	}
	
	public void setDrug(final Drug drug) {
		this.drug = drug;
	}
	
	public String getBarcode() {
		return this.barcode;
	}
	
	public void setBarcode(final String barcode) {
		this.barcode = barcode;
	}
	
	public Double getTotalQuantity() {
		return this.totalQuantity;
	}
	
	public void setTotalQuantity(final Double totalQuantity) {
		this.totalQuantity = totalQuantity;
	}
	
	@Override
	public boolean equals(final Object obj) {
		
		if (obj == null) {
			return false;
		}
		
		if (obj instanceof DrugPackage) {
			final DrugPackage other = (DrugPackage) obj;
			return new EqualsBuilder().append(this.getDrug(), other.getDrug())
			        .append(this.getBarcode(), other.getBarcode()).isEquals();
		}
		
		return false;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = (prime * result) + ((this.barcode == null) ? 0 : this.barcode.hashCode());
		result = (prime * result) + ((this.drug == null) ? 0 : this.drug.hashCode());
		return result;
	}
	
	@Override
	public int compareTo(final DrugPackage other) {
		return new CompareToBuilder().append(this.drug.getId(), other.getId())
		        .append(this.totalQuantity, other.getTotalQuantity()).toComparison();
	}
}
