package org.openmrs.module.inventorypoc.web.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DeliverNoteRow implements Serializable {
	
	private static final long serialVersionUID = 7650980841224361467L;
	
	private final String healthFacilityName;
	
	private final String deliveredDate;
	
	private final String originDocument;
	
	private final String simamDocument;
	
	private final List<DeliverNoteItemRow> items;
	
	public DeliverNoteRow(final String healthFacilityName, final String deliveredDate, final String originalDocument,
	    final String simamDocument) {
		this.healthFacilityName = healthFacilityName;
		this.deliveredDate = deliveredDate;
		this.originDocument = originalDocument;
		this.simamDocument = simamDocument;
		this.items = new ArrayList<>();
	}
	
	public String getHealthFacilityName() {
		return this.healthFacilityName;
	}
	
	public String getDeliveredDate() {
		return this.deliveredDate;
	}
	
	public String getOriginDocument() {
		return this.originDocument;
	}
	
	public String getSimamDocument() {
		return this.simamDocument;
	}
	
	public void addDeliverNoteItemRow(final DeliverNoteItemRow item) {
		this.items.add(item);
	}
	
	public List<DeliverNoteItemRow> getItems() {
		return this.items;
	}
}
