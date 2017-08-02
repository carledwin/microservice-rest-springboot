package com.carledwinti.springbootrestsecurity.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.carledwinti.springbootrestsecurity.model.Product;

@Component
public class ProductValidator implements Validator{

	private final InventoryService inventoryService;
	
	@Autowired
	public ProductValidator(InventoryService inventoryService){
		this.inventoryService = inventoryService;
	}
	
	@Override
	public boolean supports(Class<?>  clazz) {
		return Product.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		Product product = (Product) target;
		
		if(!inventoryService.isValidInventory(product.getInventoryId())){
			errors.reject("inventoryId", "inventory.id.invalid", "ID do estoque inválido.");
		}
	}

}
