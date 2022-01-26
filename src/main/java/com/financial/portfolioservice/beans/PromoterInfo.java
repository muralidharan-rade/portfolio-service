package com.financial.portfolioservice.beans;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PromoterInfo {

	@NotNull(message = "acceptable values between 0 to 100")
	double integrity;

	boolean successionRobust;

	double promoterStake;

	boolean frequentEquityDilutions;

	boolean frequentInsiderSell;

	boolean competant;

}
