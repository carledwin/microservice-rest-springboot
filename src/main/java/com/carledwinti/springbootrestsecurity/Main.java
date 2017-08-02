package com.carledwinti.springbootrestsecurity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import com.carledwinti.springbootrestsecurity.model.Product;
import com.carledwinti.springbootrestsecurity.repository.ProductRepository;

@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan
public class Main {

	private static ProductRepository repository;
	private static Logger logger = LoggerFactory.getLogger(Main.class) ;
	
	public static void main(String[] args) {
		ApplicationContext ctx = SpringApplication.run(Main.class, args);
		repository = ctx.getBean(ProductRepository.class);
		
		mockProduct(ctx);
		queryProduct(ctx);
	}

	private static void queryProduct(ApplicationContext ctx) {
		Iterable<Product> findAll = repository.findAll();
		if(findAll != null){
			for(Product product :findAll){
				logger.info("Product >>>: " + product.getId());
			}
		}
	}

	private static void mockProduct(ApplicationContext ctx) {
		repository.save(new Product("Notebook Levnovo", "Notebook Lenovo core 5", "Notebook Lenovo core 5 4GB de RAM", "54355/4M"));
	}
}
