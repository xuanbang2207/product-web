package com.home.model;

import java.util.Date;

import com.home.domain.Customer;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {
	private Long orderId;

	@DateTimeFormat(pattern = "MM/dd/yyyy")
	private Date orderDate;
	private Long customerId;
	private double amount;
	private String description;
	private String address;
	private String name;

	private Boolean isEdit = false;
	private Customer customer;
}
