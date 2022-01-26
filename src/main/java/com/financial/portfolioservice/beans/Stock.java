package com.financial.portfolioservice.beans;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIgnore;

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

	double quantity;

	@JsonIgnore
	double weightage;

	Financials financials;

	PromoterInfo promoterInfo;

	BussinessInfo businessInfo;
	
	RankingScore score;

	String sector;

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("name :: " + name).append(" quote :: " + quote).append(" current price : " + currentPrice)
				.append(" weight::   " + weightage);
		return builder.toString();
	}

	@Override
	public boolean equals(Object obj) {
		return this.quote.equalsIgnoreCase(((Stock) obj).getQuote());
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

}
