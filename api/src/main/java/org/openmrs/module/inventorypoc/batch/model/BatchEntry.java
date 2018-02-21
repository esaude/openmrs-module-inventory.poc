/**
 *
 */
package org.openmrs.module.inventorypoc.batch.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.openmrs.Order;
import org.openmrs.module.inventorypoc.common.model.BaseOpenmrsMetadataWrapper;

@Entity
@Table(name = "pocinv_batch_entry")
public class BatchEntry extends BaseOpenmrsMetadataWrapper implements Comparable<BatchEntry> {

	private static final long serialVersionUID = 1641086848809529280L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "batch_entry_id")
	private Integer batchEntryId;

	@Column(name = "quantity", nullable = false)
	private Double quantity;

	@ManyToOne
	@JoinColumn(name = "batch_id")
	private Batch batch;

	@OneToOne
	@JoinColumn(name = "reversal_requestor_id")
	private BatchEntry reversalRequestor;

	@OneToOne
	@JoinColumn(name = "reversed_id")
	private BatchEntry reversed;

	@Enumerated(EnumType.STRING)
	@Column(name = "batch_operation_type", nullable = false)
	private BatchOperationType batchOperationType;

	@ManyToOne
	@JoinColumn(name = "order_id")
	private Order order;

	public enum BatchOperationType {

		ADJUSTMENT, DISPOSED, DISTRIBUTION, RECEIPT, RETURN, TRANSFER, INITIAL
	}

	public BatchEntry() {
	}

	public BatchEntry(final Batch batch, final BatchOperationType batchOperationType, final Double quantity) {
		this.setBatch(batch);
		this.setBatchOperationType(batchOperationType);
		this.setQuantity(quantity);
	}

	@Override
	public Integer getId() {
		return this.batchEntryId;
	}

	@Override
	public void setId(final Integer batchEntryId) {
		this.batchEntryId = batchEntryId;
	}

	public Double getQuantity() {
		return this.quantity;
	}

	public void setQuantity(final Double quantity) {
		this.quantity = quantity;
	}

	public Batch getBatch() {
		return this.batch;
	}

	public void setBatch(final Batch batch) {
		this.batch = batch;
	}

	public BatchEntry getReversalRequestor() {
		return this.reversalRequestor;
	}

	public void setReversalRequestor(final BatchEntry reversalRequestor) {
		this.reversalRequestor = reversalRequestor;
	}

	public BatchEntry getReversed() {
		return this.reversed;
	}

	public void setReversed(final BatchEntry reversed) {
		this.reversed = reversed;
	}

	public BatchOperationType getBatchOperationType() {
		return this.batchOperationType;
	}

	public void setBatchOperationType(final BatchOperationType batchOperationType) {
		this.batchOperationType = batchOperationType;
	}

	public Order getOrder() {
		return this.order;
	}

	public void setOrder(final Order order) {
		this.order = order;
	}

	public boolean isReversed() {
		return this.reversalRequestor != null;
	}

	@Override
	public int compareTo(final BatchEntry other) {

		return new CompareToBuilder().append(this.getBatch(), other.getBatch())
				.append(this.getBatchOperationType(), other.getBatchOperationType())
				.append(this.getQuantity(), other.getQuantity()).append(this.getReversed(), other.getReversed())
				.append(this.getReversalRequestor(), other.getReversalRequestor()).append(this.order, other.getOrder())
				.toComparison();
	}
}
