package com.financial.portfolioservice.beans;

import java.math.BigDecimal;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Valid
public class EducationPlan {

	@NotNull
	@Positive
	private long presentExpense;

	@NotNull
	@Min(0)
	private int childPresentAge;

	@NotNull
	@Min(0)
	private int childFutureAge;

	@PositiveOrZero
	private float educationaryInflation;

	@PositiveOrZero
	private float rateOfReturn;

	private BigDecimal actualExpense;

	@NotNull
	@Min(0)
	private long presentSavingsForEducation;

	private BigDecimal monthlySavingsNeeded;

	private int availableRunway;

}
