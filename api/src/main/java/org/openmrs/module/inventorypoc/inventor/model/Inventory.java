package org.openmrs.module.inventorypoc.inventor.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.openmrs.module.inventorypoc.batch.model.Batch;
import org.openmrs.module.inventorypoc.common.model.BaseOpenmrsMetadataWrapper;

@Entity
@Table(name = "pocinv_inventory")
public class Inventory extends BaseOpenmrsMetadataWrapper {
	
	private static final long serialVersionUID = -4802073912709297203L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "inventory_id")
	private Integer inventoryId;
	
	@Column(name = "inventory_date")
	private Date inventoryDate;
	
	@OneToOne
	@JoinColumn(name = "batch_id")
	private Batch batch;
	
	@Column(name = "phisical_count")
	private Double phisicalCount;
	
	@Column(name = "system_count")
	private Double systemCount;
	
	@Column(name = "inventory_reason")
	private String inventoryReason;
	
	@Override
	public Integer getId() {
		return this.inventoryId;
	}
	
	@Override
	public void setId(final Integer inventoryId) {
		this.inventoryId = inventoryId;
	}
	
	public Date getInventoryDate() {
		return this.inventoryDate;
	}
	
	public void setInventoryDate(final Date inventoryDate) {
		this.inventoryDate = inventoryDate;
	}
	
	public Batch getBatch() {
		return this.batch;
	}
	
	public void setBatch(final Batch batch) {
		this.batch = batch;
	}
	
	public Double getPhisicalCount() {
		return this.phisicalCount;
	}
	
	public void setPhisicalCount(final Double phisicalCount) {
		this.phisicalCount = phisicalCount;
	}
	
	public Double getSystemCount() {
		return this.systemCount;
	}
	
	public void setSystemCount(final Double systemCount) {
		this.systemCount = systemCount;
	}
	
	public String getInventoryReason() {
		return this.inventoryReason;
	}
	
	public void setInventoryReason(final String inventoryReason) {
		this.inventoryReason = inventoryReason;
	}
}
