package com.carledwinti.springbootrestsecurity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.carledwinti.springbootrestsecurity.model.Product;

@Repository
public interface ProductRepository extends PagingAndSortingRepository<Product, Integer> {

	@Query("SELECT product FROM Product product WHERE (product.name) LIKE UPPER(?1) OR UPPER(product.longDescription) LIKE UPPER(?1)") List search(String term);
}
