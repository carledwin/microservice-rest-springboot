package com.carledwinti.springbootrestsecurity.restcontroller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.DataBinder;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.carledwinti.springbootrestsecurity.model.Product;
import com.carledwinti.springbootrestsecurity.repository.ProductRepository;
import com.carledwinti.springbootrestsecurity.validator.ProductValidator;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/products")//endpoint Rest
public class ProductResource {

	private final ProductRepository repository;
	private final ProductValidator validator;
	private final ObjectMapper objectMapper;
	
	@Autowired
	public ProductResource(ProductRepository repository, ProductValidator validator, ObjectMapper objectMapper){
		this.repository = repository;
		this.validator = validator;
		this.objectMapper = objectMapper;
	}
	
	@InitBinder
	protected void initBinder(WebDataBinder binder){
		binder.addValidators(validator);
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public Iterable findAll(@RequestParam(value="page", defaultValue="0") int page, @RequestParam(value="count", defaultValue="10") int count, @RequestParam(value="order", defaultValue="ASC", required=false) Sort.Direction direction, @RequestParam(value="sort", defaultValue="name", required=false) String sortProperty){
		Page result = repository.findAll(new PageRequest(page, count, new Sort(direction, sortProperty)));
		return result.getContent();
	}
	
	@RequestMapping(value="/{id}", method=RequestMethod.GET)
	public Product find(@PathVariable Integer id){
		Product product = repository.findOne(id);
		if(product == null){
			throw new ProductNotFoundException();
		}else{
			return product;
		}
	}
	
	@RequestMapping(method=RequestMethod.POST)
	public Product create(@RequestBody @Valid Product product){
		return repository.save(product);
	}
	
	@RequestMapping(value="/id", method=RequestMethod.PUT)
	public HttpEntity<?> update(@PathVariable Integer id, HttpServletRequest request) throws IOException{
		Product product = find(id);
		Product productUpdate = objectMapper.readerForUpdating(product).readValue(request.getReader());
		
		MutablePropertyValues mutablePropertyValues = new MutablePropertyValues();
		mutablePropertyValues.add("id", productUpdate.getId());
		mutablePropertyValues.add("name", productUpdate.getName());
		mutablePropertyValues.add("shortDescription", productUpdate.getShortDescription());
		mutablePropertyValues.add("longDescription", productUpdate.getLongDescription());
		mutablePropertyValues.add("inventoryId", productUpdate.getInventoryId());
		
		DataBinder dataBinder = new DataBinder(productUpdate);
		dataBinder.addValidators(validator);
		dataBinder.bind(mutablePropertyValues);
		dataBinder.validate();
		
		if(dataBinder.getBindingResult().hasErrors()){
			return new ResponseEntity<>(dataBinder.getBindingResult().getAllErrors(), HttpStatus.BAD_REQUEST);
		}else{
			return new ResponseEntity<Product>(productUpdate, HttpStatus.ACCEPTED);
		}
	}
	
	@RequestMapping(value="/{id}", method=RequestMethod.DELETE)
	public HttpEntity<?> delete(@PathVariable Integer id){
		Product product = find(id);
		repository.delete(product);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	@ResponseStatus(HttpStatus.NOT_FOUND)
	static class ProductNotFoundException extends RuntimeException{
		
		private static final long serialVersionUID = 1L;

		public ProductNotFoundException() {
		}
		
		public ProductNotFoundException(String message) {
			super(message);
		}
	}
}
