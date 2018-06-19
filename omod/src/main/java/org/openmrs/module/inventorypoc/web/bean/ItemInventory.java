package org.openmrs.module.inventorypoc.web.bean;

import java.io.Serializable;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.openmrs.module.inventorypoc.batch.model.Batch;
import org.openmrs.module.inventorypoc.delivernote.model.DeliverNoteItem;

public class ItemInventory implements Serializable, Comparable<ItemInventory> {
	
	private static final long serialVersionUID = -1148641711447839190L;
	
	private final Batch batch;
	
	private boolean selected;
	
	private Double newQuantity;
	
	private String reason;
	
	private final DeliverNoteItem noteItem;
	
	public ItemInventory(final Batch batch, final DeliverNoteItem deliverNoteItem) {
		this.batch = batch;
		this.noteItem = deliverNoteItem;
	}
	
	public boolean isSelected() {
		return this.selected;
	}
	
	public void setSelected(final boolean selected) {
		this.selected = selected;
	}
	
	public Double getNewQuantity() {
		return this.newQuantity;
	}
	
	public void setNewQuantity(final Double newQuantity) {
		this.newQuantity = newQuantity;
	}
	
	public String getReason() {
		return this.reason;
	}
	
	public void setReason(final String reason) {
		this.reason = reason;
	}
	
	public Batch getBatch() {
		return this.batch;
	}
	
	public DeliverNoteItem getNoteItem() {
		return this.noteItem;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result) + ((this.batch == null) ? 0 : this.batch.hashCode());
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
		final ItemInventory other = (ItemInventory) obj;
		if (this.batch == null) {
			if (other.batch != null) {
				return false;
			}
		} else if (!this.batch.equals(other.batch)) {
			return false;
		}
		return true;
	}
	
	@Override
	public int compareTo(final ItemInventory o) {
		
		return new CompareToBuilder().append(this.getBatch(), o.getBatch()).toComparison();
	}
}
