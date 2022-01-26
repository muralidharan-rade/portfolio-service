package com.financial.portfolioservice.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.financial.portfolioservice.beans.Portfolio;
import com.financial.portfolioservice.beans.RankingScore;
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

		scoreStocksOnBusiness(input);
		scoreStocksOnPromoterData(input);
		scoreStocksOnFinancials(input);

		allocateBasedOnFinalRanking(input);
	}

	private void allocateBasedOnFinalRanking(Portfolio input) {
		double allocationAmount = input.getInvestmentAmount();
		input.getPortfolioStocks().forEach(xStock -> {
			System.out.println("stock :: " + xStock.getQuote() + " :: " + xStock.getScore());
		});

		double totalRatio = 0;
		for (Stock x : input.getPortfolioStocks()) {
			totalRatio += x.getScore().getBusinessScore() + x.getScore().getPromoterScore()
					+ x.getScore().getFinancialScore();
		}
		System.out.println("total  ratio :: " + totalRatio);

		for (Stock x : input.getPortfolioStocks()) {
			x.setWeightage((x.getScore().getBusinessScore() + x.getScore().getPromoterScore()
					+ x.getScore().getFinancialScore()) / totalRatio);
		}

		for (Stock x : input.getPortfolioStocks()) {
			x.setQuantity(Math.round(allocationAmount * x.getWeightage() / x.getCurrentPrice()));
		}
	}

	private void scoreStocksOnBusiness(Portfolio input) {
		input.getPortfolioStocks().stream().forEach(xStock -> {
			RankingScore score = new RankingScore();
			xStock.setScore(score);
			if (xStock.getBusinessInfo().isMarketLeader()) {
				score.updateBusinessScore(1);
			}
			if (xStock.getBusinessInfo().isMonopoly()) {
				score.updateBusinessScore(1);
			}
			if (xStock.getBusinessInfo().isIdentifiableMoats()) {
				score.updateBusinessScore(1);
			}
			if (xStock.getBusinessInfo().isMarketConsolidated()) {
				score.updateBusinessScore(1);
			}
			if (xStock.getBusinessInfo().getAddressableMarketSize() >= 5) {
				score.updateBusinessScore(1);
			}
		});
	}

	private void scoreStocksOnPromoterData(Portfolio input) {
		input.getPortfolioStocks().forEach(xStock -> {
			if (xStock.getPromoterInfo().isCompetant()) {
				xStock.getScore().updatePromoterScore(1);
			}
			if (!xStock.getPromoterInfo().isFrequentInsiderSell()) {
				xStock.getScore().updatePromoterScore(1);
			}
			if (!xStock.getPromoterInfo().isFrequentEquityDilutions()) {
				xStock.getScore().updatePromoterScore(1);
			}
			if (xStock.getPromoterInfo().isSuccessionRobust()) {
				xStock.getScore().updatePromoterScore(1);
			}

			double stake = xStock.getPromoterInfo().getPromoterStake();
			if (xStock.getSector().equalsIgnoreCase(PortfolioConstants.BANKING) && stake > 20) {
				xStock.getScore().updatePromoterScore(1);
			} else {
				xStock.getScore().updatePromoterScore(0.5);
			}

			if (!xStock.getSector().equalsIgnoreCase(PortfolioConstants.BANKING) && stake > 70) {
				xStock.getScore().updatePromoterScore(1);
			} else if (!xStock.getSector().equalsIgnoreCase(PortfolioConstants.BANKING) && stake > 50) {
				xStock.getScore().updatePromoterScore(0.75);
			} else if (!xStock.getSector().equalsIgnoreCase(PortfolioConstants.BANKING) && stake > 35) {
				xStock.getScore().updatePromoterScore(0.50);
			} else {
				xStock.getScore().updatePromoterScore(0.25);
			}
		});
	}

	private void scoreStocksOnFinancials(Portfolio input) {
		getCommonFinancialScoring(input);
		scoreFinancialStocks(input);
		scoreNonFinancialStocks(input);
	}

	private void scoreNonFinancialStocks(Portfolio input) {
		input.getPortfolioStocks().forEach(xStock -> {
			if (xStock.getFinancials().getDebtToEquity() == 0) {
				xStock.getScore().updateFinancialScore(1);
			} else if (xStock.getFinancials().getDebtToEquity() < 0.5) {
				xStock.getScore().updateFinancialScore(0.5);
			}
		});

	}

	private void scoreFinancialStocks(Portfolio input) {
		input.getPortfolioStocks().forEach(xStock -> {
			xStock.getScore().updateFinancialScore(xStock.getFinancials().getRoe() / 10);
			xStock.getScore().updateFinancialScore(xStock.getFinancials().getCasa() / 10);
			xStock.getScore().updateFinancialScore(xStock.getFinancials().getCARatio() / 10);
			if (xStock.getBusinessInfo().isMarketLeader()) {

			} else {

			}
		});
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

	private void getCommonFinancialScoring(Portfolio input) {
		input.getPortfolioStocks().forEach(xStock -> {
			if (xStock.getFinancials().getRevenueGrowth3YR() > 25) {
				xStock.getScore().updateFinancialScore(1);
			} else if (xStock.getFinancials().getRevenueGrowth3YR() > 20) {
				xStock.getScore().updateFinancialScore(0.75);
			} else if (xStock.getFinancials().getRevenueGrowth3YR() > 15) {
				xStock.getScore().updateFinancialScore(0.5);
			}
		});
		input.getPortfolioStocks().forEach(xStock -> {
			if (xStock.getFinancials().getNetProfitGrowth3YR() > 25) {
				xStock.getScore().updateFinancialScore(1);
			} else if (xStock.getFinancials().getNetProfitGrowth3YR() > 20) {
				xStock.getScore().updateFinancialScore(0.75);
			} else if (xStock.getFinancials().getNetProfitGrowth3YR() > 15) {
				xStock.getScore().updateFinancialScore(0.5);
			}
		});
		input.getPortfolioStocks().forEach(xStock -> {
			if (xStock.getFinancials().getNetProfitMargin() > 25) {
				xStock.getScore().updateFinancialScore(1);
			} else if (xStock.getFinancials().getNetProfitMargin() > 20) {
				xStock.getScore().updateFinancialScore(0.75);
			} else if (xStock.getFinancials().getNetProfitMargin() > 15) {
				xStock.getScore().updateFinancialScore(0.5);
			}
		});
	}

}
