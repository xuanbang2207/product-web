package com.home.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailDto  {
    private Long orderDetailId;
    private Long orderId;
    private Long productId;
    private int quantity;
    private double unitPrice;
    private String productName;
    private double discount;

}
