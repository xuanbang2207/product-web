package com.home.model;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountDto implements Serializable{

	@NotEmpty
	private String name;

	@NotEmpty
	private String password;

	private Boolean rememberMe;
	private Boolean isEdit = false;
}
