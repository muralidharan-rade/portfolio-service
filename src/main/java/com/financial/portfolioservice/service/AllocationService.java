package com.financial.portfolioservice.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.financial.portfolioservice.beans.Portfolio;
import com.financial.portfolioservice.beans.Stock;
import com.financial.portfolioservice.util.CSVReaderUtil;
import com.financial.portfolioservice.util.PortfolioConstants;

@Component
public class AllocationService {

	@Autowired
	CSVReaderUtil csvReaderUtil;

	public Portfolio allocatePortfolio(Portfolio input) {

		if (input.getAllocationType().equalsIgnoreCase(PortfolioConstants.EQUAL_WEIGHT_METHOD)) {
			allocateByEqualWeight(input);
		} else if (input.getAllocationType().equalsIgnoreCase(PortfolioConstants.INDEX_METHOD)) {
			allocateByIndex(input);
		} else if (input.getAllocationType().equalsIgnoreCase(PortfolioConstants.CUSTOM_METHOD)) {
			allocateByCustomRules(input);
		}

		return input;
	}

	private void allocateByCustomRules(Portfolio input) {
		filterOutInferiorStocks(input);
		scoreStocksOnFinancials(input);
		scoreStocksOnPromoterData(input);
		scoreStocksOnBusiness(input);
		allocateBasedOnFinalRanking(input);
	}

	private void allocateBasedOnFinalRanking(Portfolio input) {

	}

	private void scoreStocksOnBusiness(Portfolio input) {

	}

	private void scoreStocksOnPromoterData(Portfolio input) {

	}

	private void scoreStocksOnFinancials(Portfolio input) {

	}

	// currently the minimum integrity score is set to 75
	private void filterOutInferiorStocks(Portfolio input) {
		input.getPortfolioStocks().removeIf(x -> x.getPromoterInfo().getIntegrity() < 75);
	}

	private void allocateByEqualWeight(Portfolio input) {

		double allocationAmount = input.getInvestmentAmount();
		int stockCount = input.getPortfolioStocks().size();
		double amountPerStock = allocationAmount / stockCount;

		for (Stock stock : input.getPortfolioStocks()) {
			stock.setQuantity(Math.round(amountPerStock / stock.getCurrentPrice()));
		}
	}

	/**
	 * Assumption, user inputs only stocks which are in index like NIFTY50
	 * 
	 * https://www1.nseindia.com/live_market/dynaContent/live_watch/equities_stock_watch.htm
	 * - download csv (weightage is not available)
	 * 
	 **/
	private void allocateByIndex(Portfolio input) {

		double allocationAmount = input.getInvestmentAmount();

		updateStocksWithIndexWeightage(input);
		for (Stock stock : input.getPortfolioStocks()) {
			stock.setQuantity(Math.round(allocationAmount * (stock.getWeightage() / 100) / stock.getCurrentPrice()));
		}
	}

	private void updateStocksWithIndexWeightage(Portfolio input) {
		try {
			List<Stock> stockList = input.getPortfolioStocks();
			List<Stock> csvIndexStocks = csvReaderUtil.csvToBean();

			stockList.parallelStream().forEach(x -> {
				csvIndexStocks.parallelStream().filter(y -> {
					return y.equals(x);
				}).limit(1).forEach(y -> {
					x.setWeightage(y.getWeightage());
				});
			});

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
