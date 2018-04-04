package org.openmrs.module.inventorypoc.web.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.openmrs.api.context.Context;
import org.openmrs.module.inventorypoc.batch.model.Batch;
import org.openmrs.module.inventorypoc.batch.service.BatchService;
import org.openmrs.module.inventorypoc.inventor.model.Inventory;
import org.openmrs.module.inventorypoc.inventor.service.InventoryService;
import org.openmrs.module.inventorypoc.web.bean.ItemInventory;
import org.openmrs.module.inventorypoc.web.bean.StockAdjust;
import org.openmrs.module.inventorypoc.web.utils.WebUtils;
import org.openmrs.module.inventorypoc.web.validator.InventoryValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;

@Controller(StockAdjustmentController.MODULE_ID + ".StockAdjustment")
@SessionAttributes({ "inventory" })
public class StockAdjustmentController {
	
	public static final String MODULE_ID = "inventorypoc";
	
	@Autowired
	private InventoryValidator inventoryValidator;
	
	@RequestMapping(value = { "/module/inventorypoc/stockAdjustment/initForm" }, method = RequestMethod.GET)
	public ModelAndView initForm(final SessionStatus status, final Model model) {
		final List<Batch> availableStock = Context.getService(BatchService.class)
		        .findAllAvailableStock(Context.getLocationService().getDefaultLocation(), new Date());
		
		final StockAdjust inventory = this.toInventory(availableStock);
		model.addAttribute("inventory", inventory);
		status.setComplete();
		
		return new ModelAndView();
	}
	
	@RequestMapping(value = { "/module/inventorypoc/stockAdjustment/initForm" }, method = RequestMethod.POST)
	public ModelAndView initFormPost(@ModelAttribute("inventory") final StockAdjust inventory, final Errors errors) {
		
		this.inventoryValidator.validate(inventory, errors);
		
		if (errors.hasErrors()) {
			return new ModelAndView();
		}
		
		return new ModelAndView(WebUtils.redirect("adjust"));
	}
	
	@RequestMapping(value = { "/module/inventorypoc/stockAdjustment/adjust" }, method = RequestMethod.GET)
	public ModelAndView adjust() {
		
		return new ModelAndView();
	}
	
	@RequestMapping(value = { "/module/inventorypoc/stockAdjustment/adjust" }, method = RequestMethod.POST)
	public ModelAndView adjustPost(@ModelAttribute("inventory") final StockAdjust stockAdjust) {
		
		final InventoryService inventoryService = Context.getService(InventoryService.class);
		for (final ItemInventory item : stockAdjust.getItems()) {
			
			if (item.isSelected()) {
				final Inventory inventory = new Inventory();
				inventory.setBatch(item.getBatch());
				inventory.setPhisicalCount(item.getNewQuantity());
				inventory.setInventoryReason(item.getReason());
				inventory.setInventoryDate(new Date());
				inventoryService.createInventory(inventory);
			}
		}
		final ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("openmrs_msg", "inventorypoc.transactionSuccess");
		
		return modelAndView;
	}
	
	@ModelAttribute("inventory")
	public StockAdjust getVisitor(final HttpServletRequest request) {
		return this.toInventory(Context.getService(BatchService.class)
		        .findAllAvailableStock(Context.getLocationService().getDefaultLocation(), new Date()));
	}
	
	private StockAdjust toInventory(final List<Batch> batchs) {
		
		final List<ItemInventory> items = new ArrayList<>();
		for (final Batch batch : batchs) {
			items.add(new ItemInventory(batch));
		}
		return new StockAdjust(items);
	}
}
