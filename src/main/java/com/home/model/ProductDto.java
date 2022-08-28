package com.home.model;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto implements Serializable {
	private Long productId;
	@NotEmpty(message = "Tên không được để trống")
	@Length(min = 4, message = "Tên phải từ 4 ký tự trở lên")
	private String name;
	
	private int quantity;
	
	private double unitPrice;
	private String image;

	private MultipartFile imageFile;

	private Boolean available = true;
	private Integer viewCount = 0;

	private String description;
	
	private double discount;

	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date enteredDate = new Date();

	private Long categoryId;
	private Boolean isEdit = false;
	private String categoryName;

}
