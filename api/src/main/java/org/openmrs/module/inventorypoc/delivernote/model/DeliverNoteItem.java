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
package org.openmrs.module.inventorypoc.delivernote.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.apache.commons.lang3.StringUtils;
import org.openmrs.module.inventorypoc.batch.model.Batch;
import org.openmrs.module.inventorypoc.common.model.BaseOpenmrsMetadataWrapper;
import org.openmrs.module.inventorypoc.drugpackage.model.DrugPackage;

@Entity
@Table(name = "pocinv_deliver_note_item", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "origin_document_code", "simam_number", "drug_package_id" }) })
public class DeliverNoteItem extends BaseOpenmrsMetadataWrapper {
	
	private static final long serialVersionUID = -5334607210286652512L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "deliver_note_item_id")
	private Integer deliverNoteItemId;
	
	@ManyToOne
	@JoinColumn(name = "deliver_note_id")
	private DeliverNote deliverNote;
	
	@ManyToOne
	@JoinColumn(name = "drug_package_id")
	private DrugPackage drugPackage;
	
	@Column(name = "requested_quantity")
	private Double requestedQuantity;
	
	@Column(name = "quantity")
	private Double quantity;
	
	@Column(name = "unit_price")
	private Double unitPrice;
	
	@Column(name = "expire_date")
	private Date expireDate;
	
	@OneToOne
	@JoinColumn(name = "batch_id")
	private Batch batch;
	
	@Column(name = "authorized_quantity")
	private Double authorizedQuantity;
	
	@Column(name = "origin_document_code")
	private String originDocumentCode;
	
	@Column(name = "simam_number")
	private String simamNumber;
	
	@Transient
	private Action action;
	
	public enum Action {
		INSERT, UPDATE
	}
	
	public DeliverNoteItem() {
	}
	
	public DeliverNoteItem(final Integer id) {
		this.deliverNoteItemId = id;
	}
	
	@Override
	public Integer getId() {
		return this.deliverNoteItemId;
	}
	
	@Override
	public void setId(final Integer deliverNoteItemId) {
		this.deliverNoteItemId = deliverNoteItemId;
	}
	
	public DeliverNote getDeliverNote() {
		return this.deliverNote;
	}
	
	public void setDeliverNote(final DeliverNote deliverNote) {
		this.deliverNote = deliverNote;
	}
	
	public DrugPackage getDrugPackage() {
		return this.drugPackage;
	}
	
	public void setDrugPackage(final DrugPackage drugPackage) {
		this.drugPackage = drugPackage;
	}
	
	public Double getQuantity() {
		return this.quantity;
	}
	
	public void setQuantity(final Double quantity) {
		this.quantity = quantity;
	}
	
	public Double getUnitPrice() {
		return this.unitPrice;
	}
	
	public void setUnitPrice(final Double unitPrice) {
		this.unitPrice = unitPrice;
	}
	
	public Date getExpireDate() {
		return this.expireDate;
	}
	
	public void setExpireDate(final Date expireDate) {
		this.expireDate = expireDate;
	}
	
	public Batch getBatch() {
		return this.batch;
	}
	
	public void setBatch(final Batch batch) {
		this.batch = batch;
	}
	
	public Double getRequestedQuantity() {
		return this.requestedQuantity;
	}
	
	public void setRequestedQuantity(final Double requestedQuantity) {
		this.requestedQuantity = requestedQuantity;
	}
	
	public Double getAuthorizedQuantity() {
		return this.authorizedQuantity;
	}
	
	public void setAuthorizedQuantity(final Double authorizedQuantity) {
		this.authorizedQuantity = authorizedQuantity;
	}
	
	public String getOriginDocumentCode() {
		return this.originDocumentCode;
	}
	
	public void setOriginDocumentCode(final String origenDocumentCode) {
		this.originDocumentCode = StringUtils.upperCase(origenDocumentCode);
	}
	
	public String getSimamNumber() {
		return this.simamNumber;
	}
	
	public void setSimamNumber(final String simamNumber) {
		this.simamNumber = StringUtils.upperCase(simamNumber);
	}
	
	public Action getAction() {
		return this.action;
	}
	
	public void setAction(final Action action) {
		this.action = action;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result) + ((this.batch == null) ? 0 : this.batch.hashCode());
		result = (prime * result) + ((this.deliverNote == null) ? 0 : this.deliverNote.hashCode());
		result = (prime * result) + ((this.drugPackage == null) ? 0 : this.drugPackage.hashCode());
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
		final DeliverNoteItem other = (DeliverNoteItem) obj;
		if (this.batch == null) {
			if (other.batch != null) {
				return false;
			}
		} else if (!this.batch.equals(other.batch)) {
			return false;
		}
		if (this.deliverNote == null) {
			if (other.deliverNote != null) {
				return false;
			}
		} else if (!this.deliverNote.equals(other.deliverNote)) {
			return false;
		}
		if (this.drugPackage == null) {
			if (other.drugPackage != null) {
				return false;
			}
		} else if (!this.drugPackage.equals(other.drugPackage)) {
			return false;
		}
		return true;
	}
}
