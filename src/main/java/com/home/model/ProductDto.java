package com.home.model;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotEmpty;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto  {
    private Long productId;
 
    private String name;

    private int quantity;

    private double unitPrice;
    private String image;

    private MultipartFile imageFile;
    
    private Boolean available;
    private Integer viewCount;
    
    private String description;
    private double discount;
    private Date enteredDate;
    private short status;
    private Long categoryId;
    private Boolean isEdit;
	private String categoryName;

	

}
