package com.carledwinti.springbootrestsecurity.restcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.carledwinti.springbootrestsecurity.repository.ProductRepository;

@RestController
@RequestMapping("/search")
public class ProductSearchResource {

	private final ProductRepository repository;
	
	@Autowired
	public ProductSearchResource(ProductRepository repository) {
		this.repository = repository;
	}
	
	/*@RequestMapping(method=RequestMethod.GET)
	public List search(@RequestParam("q") String queryTerm){
		List product = repository.search("%" + queryTerm + "%");
		return product
	}*/
}
