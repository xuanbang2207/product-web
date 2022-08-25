package com.home.model;

import javax.validation.constraints.NotEmpty;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdimAccount {
	@NotEmpty
    private String name;
    
    @NotEmpty
    private String password;

    private Boolean rememberMe;
    private Boolean isEdit = false;
	
}
