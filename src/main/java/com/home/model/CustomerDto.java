package com.home.model;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDto  {
    private Long customerId;
    @NotEmpty
    private String name;
    private String email;
    @NotEmpty
    private String password;
    private String phone;
    private Date registeredDate;
    private short status;

    private Boolean isEdit = false;
}