package com.home.model;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.home.domain.Customer;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto implements Serializable {
	private Long orderId;

	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date orderDate;
	private Long customerId;
	private double amount;
	private String description;
	@NotEmpty(message = "Địa chỉ không được để trống")
	private String address;
	@NotEmpty(message = "Số điện thoại thì không được để trống")
	private String phone;
	
	private String name;

	private Boolean isEdit = false;
	private Customer customer;
}
