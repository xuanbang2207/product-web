package com.home.service;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery.FetchableFluentQuery;

import com.home.domain.Product;

public interface ProductService {

	<S extends Product> List<S> findAll(Example<S> example, Sort sort);

	<S extends Product> List<S> findAll(Example<S> example);

	void deleteAll();

	void delete(Product entity);

	void deleteById(Long id);

	<S extends Product> Page<S> findAll(Example<S> example, Pageable pageable);

	<S extends Product> List<S> saveAll(Iterable<S> entities);

	Optional<Product> findById(Long id);

	List<Product> findAllById(Iterable<Long> ids);

	List<Product> findAll(Sort sort);

	Page<Product> findAll(Pageable pageable);

	List<Product> findAll();

	<S extends Product> S save(S entity);

	List<Product> findByCategoryId(Long categoryId);

	List<Product> findBycategoryId(Long categoryId);

	List<Product> findByNameContaining(String name);

	

   

	

}
