package com.home.repository;

import java.util.List;

import com.home.domain.Customer;
import com.home.domain.Order;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface OrderRepository extends JpaRepository<Order, Long>{

	List<Order> findByCustomer(Customer user);
	
	List<Order> findByCustomerCustomerId(Long customerId);
	
	List<Order> findByCustomerCustomerId(Long customerId,Sort sort);
	
}
