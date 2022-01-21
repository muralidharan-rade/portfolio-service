package com.financial.portfolioservice.beans;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Valid
public class Portfolio {

	@NotNull
	double investmentAmount;

	@NotEmpty
	List<Stock> portfolioStocks;

	@NotEmpty(message = "cannot be null")
	String allocationType;

}
