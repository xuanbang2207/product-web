package com.home.model;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDto implements Serializable{
	private Long customerId;
	
	@NotEmpty(message =  "Tên không được để trống")
	private String name;
	
	@NotEmpty(message = "email không được để trống")
	@Email(message = "Chưa đúng định dạng email")
	private String email;
	@NotEmpty(message = "password không được để trống")
	private String password;
	
	private Boolean activated = false;
	private Boolean admin = false;

	private Boolean rememberMe;
	private Boolean isEdit = false;

}
