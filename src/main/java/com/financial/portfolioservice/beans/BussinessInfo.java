package com.financial.portfolioservice.beans;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BussinessInfo {

	boolean marketLeader;

	boolean identifiableMoats;

	boolean monopoly;

	// range of 1 to 100
	int addressableMarketSize;

	boolean isMarketConsolidated;

}
