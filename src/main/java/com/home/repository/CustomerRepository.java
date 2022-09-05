package com.home.repository;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.home.domain.Customer;
import com.home.domain.Product;
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

	Customer findByNameAndPassword(String name, String password);
	
	Optional<Customer> findByNameAndEmail(String name, String email);
    
	List<Customer> findByNameContaining(String name);
	List<Customer> findByActivated(Boolean boolean2);
	
	List<Customer> findByAdmin(Boolean boolean1);
	
	List<Customer> findByActivatedAndNameContaining(Boolean boolean2, String name);
	
	List<Customer> findByAdminAndNameContaining(Boolean boolean1, String name);
	
}
