package com.home.model;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FormChange implements Serializable{

	private String name;
	@NotEmpty(message = "Chưa nhập mật khẩu cũ")
	private String password;
	@NotEmpty(message = "Chưa nhập mật khẩu mới")
	private String password1;
	@NotEmpty(message = "Chưa nhập mật khẩu xác nhận")
	private String password2;

	private Long id;
}
