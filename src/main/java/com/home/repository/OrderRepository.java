package com.home.repository;

import java.util.List;
import java.util.Optional;

import com.home.domain.Customer;
import com.home.domain.Order;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
@Repository
public interface OrderRepository extends JpaRepository<Order, Long>{

	List<Order> findByCustomer(Customer user);
	
	List<Order> findByCustomerCustomerId(Long customerId);
	
	List<Order> findByCustomerCustomerId(Long customerId,Sort sort);
	
	List<Order> findByCustomerNameContaining(String name);
	
	@Query("select o from Order o where o.customer.name like ?1")
	List<Order> findByCustomerName(String name);
}
