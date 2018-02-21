package org.openmrs.module.inventorypoc.web.bean;

import java.io.Serializable;
import java.util.List;

public class StockAdjust implements Serializable {

	private static final long serialVersionUID = 2624288251451888925L;
	private final List<ItemInventory> items;

	public StockAdjust(final List<ItemInventory> items) {
		this.items = items;
	}

	public List<ItemInventory> getItems() {
		return this.items;
	}

}
