package com.financial.portfolioservice.beans;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Valid
public class Stock {

	@NotEmpty
	String name;
	
	@NotEmpty(message = "cannot be  null")
	String quote;
	
	@NotEmpty
	double currentPrice;
	
	double averagePrice;
	
	int quantity;

}
