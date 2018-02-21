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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.apache.commons.lang3.StringUtils;
import org.openmrs.Location;
import org.openmrs.module.inventorypoc.common.model.BaseOpenmrsMetadataWrapper;

@Entity
@Table(name = "pocinv_deliver_note", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "origin_document_code", "simam_number" }) })
public class DeliverNote extends BaseOpenmrsMetadataWrapper {
	
	/**
	 *
	 */
	private static final long serialVersionUID = -5334607210286652512L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "deliver_note_id")
	private Integer deliverNoteId;
	
	@Column(name = "origin_document_code")
	private String originDocumentCode;
	
	@Column(name = "simam_number")
	private String simamNumber;
	
	@Column(name = "recipt_date")
	private Date reciptDate;
	
	@ManyToOne
	@JoinColumn(name = "location_id")
	private Location location;
	
	@OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER, mappedBy = "deliverNote")
	private final List<DeliverNoteItem> deliverNoteItems = new ArrayList<>();
	
	public DeliverNote() {
	}
	
	public DeliverNote(final Integer id) {
		this.deliverNoteId = id;
	}
	
	@Override
	public Integer getId() {
		return this.deliverNoteId;
	}
	
	@Override
	public void setId(final Integer deliverNoteId) {
		this.deliverNoteId = deliverNoteId;
	}
	
	public Integer getDeliverNoteId() {
		return this.deliverNoteId;
	}
	
	public void setDeliverNoteId(final Integer deliverNoteId) {
		this.deliverNoteId = deliverNoteId;
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
	
	public Date getReciptDate() {
		return this.reciptDate;
	}
	
	public void setReciptDate(final Date reciptDate) {
		this.reciptDate = reciptDate;
	}
	
	public List<DeliverNoteItem> getDeliverNoteItems() {
		return this.deliverNoteItems;
	}
	
	public void AddDeliverNoteItem(final DeliverNoteItem deliverNoteItem) {
		
		this.deliverNoteItems.add(deliverNoteItem);
	}
	
	public Location getLocation() {
		return this.location;
	}
	
	public void setLocation(final Location location) {
		this.location = location;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result) + ((this.location == null) ? 0 : this.location.hashCode());
		result = (prime * result) + ((this.originDocumentCode == null) ? 0 : this.originDocumentCode.hashCode());
		result = (prime * result) + ((this.simamNumber == null) ? 0 : this.simamNumber.hashCode());
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
		final DeliverNote other = (DeliverNote) obj;
		if (this.location == null) {
			if (other.location != null) {
				return false;
			}
		} else if (!this.location.equals(other.location)) {
			return false;
		}
		if (this.originDocumentCode == null) {
			if (other.originDocumentCode != null) {
				return false;
			}
		} else if (!this.originDocumentCode.equals(other.originDocumentCode)) {
			return false;
		}
		if (this.simamNumber == null) {
			if (other.simamNumber != null) {
				return false;
			}
		} else if (!this.simamNumber.equals(other.simamNumber)) {
			return false;
		}
		return true;
	}
}
