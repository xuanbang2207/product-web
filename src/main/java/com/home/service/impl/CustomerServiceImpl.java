package com.home.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery.FetchableFluentQuery;
import org.springframework.stereotype.Service;

import com.home.domain.Customer;
import com.home.repository.CustomerRepository;
import com.home.service.CustomerService;

@Service
public class CustomerServiceImpl implements CustomerService {
    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public <S extends Customer> S save(S entity) {
	return customerRepository.save(entity);
    }

    @Override
    public List<Customer> findByNameContaining(String name) {
	return customerRepository.findByNameContaining(name);
    }
    
    @Override
    public List<Customer> findAll() {
	return customerRepository.findAll();
    }

    @Override
    public Page<Customer> findAll(Pageable pageable) {
	return customerRepository.findAll(pageable);
    }

    @Override
    public List<Customer> findAll(Sort sort) {
	return customerRepository.findAll(sort);
    }

    @Override
    public List<Customer> findAllById(Iterable<Long> ids) {
	return customerRepository.findAllById(ids);
    }

    @Override
    public Optional<Customer> findById(Long id) {
	return customerRepository.findById(id);
    }

    @Override
    public <S extends Customer> List<S> saveAll(Iterable<S> entities) {
	return customerRepository.saveAll(entities);
    }

    @Override
    public <S extends Customer> S saveAndFlush(S entity) {
	return customerRepository.saveAndFlush(entity);
    }

    @Override
    public <S extends Customer> List<S> saveAllAndFlush(Iterable<S> entities) {
	return customerRepository.saveAllAndFlush(entities);
    }

    @Override
    public <S extends Customer> Page<S> findAll(Example<S> example, Pageable pageable) {
	return customerRepository.findAll(example, pageable);
    }

    @Override
    public void deleteAllInBatch(Iterable<Customer> entities) {
	customerRepository.deleteAllInBatch(entities);
    }

    @Override
    public void deleteById(Long id) {
	customerRepository.deleteById(id);
    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Long> ids) {
	customerRepository.deleteAllByIdInBatch(ids);
    }

    @Override
    public void delete(Customer entity) {
	customerRepository.delete(entity);
    }

    @Override
    public <S extends Customer, R> R findBy(Example<S> example, Function<FetchableFluentQuery<S>, R> queryFunction) {
	return customerRepository.findBy(example, queryFunction);
    }

    @Override
    public void deleteAllById(Iterable<? extends Long> ids) {
	customerRepository.deleteAllById(ids);
    }

    @Override
    public void deleteAllInBatch() {
	customerRepository.deleteAllInBatch();
    }

    @Override
    public Customer getOne(Long id) {
	return customerRepository.getOne(id);
    }

    @Override
    public void deleteAll(Iterable<? extends Customer> entities) {
	customerRepository.deleteAll(entities);
    }

    @Override
    public void deleteAll() {
	customerRepository.deleteAll();
    }

    @Override
    public Customer getById(Long id) {
	return customerRepository.getById(id);
    }

    @Override
    public Customer getReferenceById(Long id) {
	return customerRepository.getReferenceById(id);
    }

    @Override
    public <S extends Customer> List<S> findAll(Example<S> example) {
	return customerRepository.findAll(example);
    }

    @Override
    public <S extends Customer> List<S> findAll(Example<S> example, Sort sort) {
	return customerRepository.findAll(example, sort);
    }

}
