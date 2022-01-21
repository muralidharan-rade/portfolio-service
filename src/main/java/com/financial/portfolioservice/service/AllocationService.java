package com.financial.portfolioservice.service;

import org.springframework.stereotype.Component;

import com.financial.portfolioservice.beans.Portfolio;
import com.financial.portfolioservice.beans.Stock;
import com.financial.portfolioservice.util.PortfolioConstants;

@Component
public class AllocationService {

	public Portfolio allocatePortfolio(Portfolio input) {

		if (input.getAllocationType().equalsIgnoreCase(PortfolioConstants.EQUAL_WEIGHT_METHOD)) {
			allocateByEqualWeight(input);
		}

		return input;
	}

	private void allocateByEqualWeight(Portfolio input) {

		double allocationAmount = input.getInvestmentAmount();
		int stockCount = input.getPortfolioStocks().size();
		double amountPerStock = allocationAmount / stockCount;

		for (Stock stock : input.getPortfolioStocks()) {
			stock.setQuantity((int) Math.round(amountPerStock / stock.getCurrentPrice()));
		}
	}

}
