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
package org.openmrs.module.inventorypoc.batch.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.openmrs.Location;
import org.openmrs.module.inventorypoc.common.model.BaseOpenmrsMetadataWrapper;
import org.openmrs.module.inventorypoc.drugpackage.model.DrugPackage;

@Entity
@Table(name = "pocinv_batch")
public class Batch extends BaseOpenmrsMetadataWrapper implements Comparable<Batch> {
	
	private static final long serialVersionUID = -716296434409142882L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "batch_id")
	private Integer batchId;
	
	@ManyToOne
	@JoinColumn(name = "drug_package_id")
	private DrugPackage drugPackage;
	
	@Column(name = "recipt_date")
	private Date reciptDate;
	
	@Column(name = "expire_date")
	private Date expireDate;
	
	@Column(name = "package_quantity")
	private Double packageQuantity;
	
	@Column(name = "package_quantity_units")
	private Double packageQuantityUnits;
	
	@Column(name = "remain_package_quantity_units")
	private Double remainPackageQuantityUnits;
	
	@Column(name = "unbalanced_units_quantity")
	private Double unBalancedUnitsQuantity;
	
	@ManyToOne
	@JoinColumn(name = "location_id")
	private Location location;
	
	@Column(name = "version")
	private Double version;
	
	public Batch() {
	}
	
	public Batch(final Integer id) {
		this.batchId = id;
	}
	
	@Override
	public Integer getId() {
		return this.batchId;
	}
	
	@Override
	public void setId(final Integer batchId) {
		this.batchId = batchId;
	}
	
	public DrugPackage getDrugPackage() {
		return this.drugPackage;
	}
	
	public void setDrugPackage(final DrugPackage drugPackage) {
		this.drugPackage = drugPackage;
	}
	
	public Date getReciptDate() {
		return this.reciptDate;
	}
	
	public void setReciptDate(final Date reciptDate) {
		this.reciptDate = reciptDate;
	}
	
	public Date getExpireDate() {
		return this.expireDate;
	}
	
	public void setExpireDate(final Date expireDate) {
		this.expireDate = expireDate;
	}
	
	public Double getPackageQuantity() {
		return this.packageQuantity;
	}
	
	public void setPackageQuantity(final Double packageQuantity) {
		this.packageQuantity = packageQuantity;
	}
	
	public Double getPackageQuantityUnits() {
		return this.packageQuantityUnits;
	}
	
	public void setPackageQuantityUnits(final Double packageQuantityUnits) {
		this.packageQuantityUnits = packageQuantityUnits;
	}
	
	public Double getRemainPackageQuantityUnits() {
		return this.remainPackageQuantityUnits;
	}
	
	public void setRemainPackageQuantityUnits(final Double remainPackageQuantityUnits) {
		this.remainPackageQuantityUnits = remainPackageQuantityUnits;
	}
	
	public Double getUnBalancedUnitsQuantity() {
		return this.unBalancedUnitsQuantity;
	}
	
	public void setUnBalancedUnitsQuantity(final Double unBalancedUnitsQuantity) {
		this.unBalancedUnitsQuantity = unBalancedUnitsQuantity;
	}
	
	public Location getLocation() {
		return this.location;
	}
	
	public void setLocation(final Location location) {
		this.location = location;
	}
	
	public Double getVersion() {
		return this.version;
	}
	
	public void setVersion(final Double version) {
		this.version = version;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result) + ((this.drugPackage == null) ? 0 : this.drugPackage.hashCode());
		result = (prime * result) + ((this.location == null) ? 0 : this.location.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (this.getClass() != obj.getClass()) {
			return false;
		}
		final Batch other = (Batch) obj;
		if (this.drugPackage == null) {
			if (other.drugPackage != null) {
				return false;
			}
		} else if (!this.drugPackage.equals(other.drugPackage)) {
			return false;
		}
		if (this.location == null) {
			if (other.location != null) {
				return false;
			}
		} else if (!this.location.equals(other.location)) {
			return false;
		}
		return true;
	}
	
	@Override
	public int compareTo(final Batch other) {
		return new CompareToBuilder().append(this.drugPackage, other.getDrugPackage())
		        .append(this.location.getId(), other.location.getId()).append(this.expireDate, other.expireDate)
		        .toComparison();
	}
}
