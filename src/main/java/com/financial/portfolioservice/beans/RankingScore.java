package com.financial.portfolioservice.beans;

import lombok.Getter;

@Getter
public class RankingScore {

	double businessScore;
	double financialScore;
	double promoterScore;

	public void updateBusinessScore(double x) {
		this.businessScore = this.businessScore + x;
	}

	public void updateFinancialScore(double x) {
		this.financialScore = this.financialScore + x;
	}

	public void updatePromoterScore(double x) {
		this.promoterScore = this.promoterScore + x;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("\nBusiness score: " + this.getBusinessScore());
		builder.append("\nPromoter score: " + this.getPromoterScore());
		builder.append("\nfinance  score: " + this.getFinancialScore());
		return builder.toString();
	}

}
