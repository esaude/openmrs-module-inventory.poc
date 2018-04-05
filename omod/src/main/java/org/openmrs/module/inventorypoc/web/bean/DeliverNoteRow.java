package org.openmrs.module.inventorypoc.web.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DeliverNoteRow implements Serializable {
	
	private static final long serialVersionUID = 7650980841224361467L;
	
	private final String healthFacilityName;
	
	private final String deliveryDate;
	
	private final String simamNumber;
	
	private final List<DeliverNoteItemRow> items;
	
	public DeliverNoteRow(final String healthFacilityName, final String deliveryDate, final String simamNumber) {
		this.healthFacilityName = healthFacilityName;
		this.deliveryDate = deliveryDate;
		this.simamNumber = simamNumber;
		this.items = new ArrayList<>();
	}
	
	public String getHealthFacilityName() {
		return this.healthFacilityName;
	}
	
	public String getDeliveryDate() {
		return this.deliveryDate;
	}
	
	public String getSimamNumber() {
		return this.simamNumber;
	}
	
	public void addDeliverNoteItemRow(final DeliverNoteItemRow item) {
		this.items.add(item);
	}
	
	public List<DeliverNoteItemRow> getItems() {
		return this.items;
	}
}
