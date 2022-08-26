package com.home.model;

import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FormChange {
	@NotEmpty
	private String name;
	@NotEmpty
	private String password;
	@NotEmpty
	private String password1;
	@NotEmpty
	private String password2;

	private Long id;
}
