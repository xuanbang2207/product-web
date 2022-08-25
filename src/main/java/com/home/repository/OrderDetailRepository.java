package com.home.repository;

import java.util.List;

import com.home.domain.Order;
import com.home.domain.OrderDetail;
import com.home.domain.Product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long>{

	List<OrderDetail> findByOrderOrderId(Long orderId);
	
	
}
