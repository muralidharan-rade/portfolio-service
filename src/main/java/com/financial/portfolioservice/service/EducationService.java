package com.financial.portfolioservice.service;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.financial.portfolioservice.beans.EducationPlan;

@Component
public class EducationService {

	@Value("${education.plan.default-inflation-rate}")
	private int defaultInflation;

	@Value("${education.plan.default-return-rate}")
	private int defaultRateOfReturn;

	public EducationPlan createPlan(EducationPlan educationPlan) {

		int runway = educationPlan.getChildFutureAge() - educationPlan.getChildPresentAge();
		educationPlan.setAvailableRunway(runway);

		float inflation = educationPlan.getEducationaryInflation();
		if (inflation <= 0) {
			inflation = defaultInflation;
			educationPlan.setEducationaryInflation(inflation);
		}

		float roi = educationPlan.getRateOfReturn();
		if (roi <= 0) {
			roi = defaultRateOfReturn;
			educationPlan.setRateOfReturn(roi);
		}

		BigDecimal futureAmount = calculateInflatedAmount(educationPlan.getPresentExpense(), runway, inflation);
		educationPlan.setActualExpense(futureAmount);

		BigDecimal monthlyInvestmentNeeded = calculateInvestment(futureAmount.longValue(), runway, roi,
				educationPlan.getPresentSavingsForEducation());
		educationPlan.setMonthlySavingsNeeded(monthlyInvestmentNeeded);

		return educationPlan;
	}

	private BigDecimal calculateInflatedAmount(long presentCost, int years, float rate) {

		BigDecimal cost = new BigDecimal(presentCost);
		BigDecimal inflationRate = new BigDecimal(rate);

		inflationRate = inflationRate.divide(new BigDecimal(100));

		for (int i = 1; i <= years; i++) {
			cost = cost.add(cost.multiply(inflationRate));
		}

		return cost.setScale(0, BigDecimal.ROUND_UP);
	}

	private BigDecimal calculateInvestment(long actualCost, int years, float roi, long presentSavingsForEducation) {

		BigDecimal futureValueOfAvailableSavings = calculateInflatedAmount(presentSavingsForEducation, years, roi);
		long remainingActualAmount = actualCost - futureValueOfAvailableSavings.longValue();

		int timePeriodInMonths = years * 12;
		float monthlyRate = roi / 12 / 100;

		double emi = remainingActualAmount / ((Math.pow(1 + monthlyRate, timePeriodInMonths) - 1) / monthlyRate);

		return BigDecimal.valueOf(emi).setScale(0, BigDecimal.ROUND_UP);
	}

}
