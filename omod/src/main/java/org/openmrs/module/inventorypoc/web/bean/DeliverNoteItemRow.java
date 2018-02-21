package org.openmrs.module.inventorypoc.web.bean;

import java.io.Serializable;

import org.apache.commons.lang.builder.CompareToBuilder;

public class DeliverNoteItemRow implements Serializable, Comparable<DeliverNoteItemRow> {
	
	private static final long serialVersionUID = -5553952510952178555L;
	
	private final String fnmCode;
	
	private final String designation;
	
	private final String totalPackageUnits;
	
	private final String requestedQuantity;
	
	private final String authorizedQuantity;
	
	private final String deliveredQuantity;
	
	private final String unitPrice;
	
	private final String expirationDate;
	
	private final String originDocument;
	
	private final String simamDocument;
	
	private String drugNameSytemDesignation;
	
	public DeliverNoteItemRow(final String fnmCode, final String designation, final String totalPackageUnits,
	    final String requestedQuantity, final String authorizedQuantity, final String deliveredQuantity,
	    final String unitaryPrice, final String expirationDate, final String originDocument,
	    final String simamDocument) {
		
		this.fnmCode = fnmCode;
		this.designation = designation;
		this.totalPackageUnits = totalPackageUnits;
		this.requestedQuantity = requestedQuantity;
		this.authorizedQuantity = authorizedQuantity;
		this.deliveredQuantity = deliveredQuantity;
		this.unitPrice = unitaryPrice;
		this.expirationDate = expirationDate;
		this.originDocument = originDocument;
		this.simamDocument = simamDocument;
	}
	
	public String getFnmCode() {
		return this.fnmCode;
	}
	
	public String getTotalPackageUnits() {
		return this.totalPackageUnits;
	}
	
	public String getDesignation() {
		return this.designation;
	}
	
	public String getRequestedQuantity() {
		return this.requestedQuantity;
	}
	
	public String getAuthorizedQuantity() {
		return this.authorizedQuantity;
	}
	
	public String getDeliveredQuantity() {
		return this.deliveredQuantity;
	}
	
	public String getUnitPrice() {
		return this.unitPrice;
	}
	
	public String getExpirationDate() {
		return this.expirationDate;
	}
	
	public String getDrugNameSytemDesignation() {
		return this.drugNameSytemDesignation;
	}
	
	public void setDrugNameSytemDesignation(final String drugNameSytemDesignation) {
		this.drugNameSytemDesignation = drugNameSytemDesignation;
	}
	
	public String getOriginDocument() {
		return this.originDocument;
	}
	
	public String getSimamDocument() {
		return this.simamDocument;
	}
	
	@Override
	public int compareTo(final DeliverNoteItemRow other) {
		return new CompareToBuilder().append(this.fnmCode, other.getFnmCode()).toComparison();
	}
}
