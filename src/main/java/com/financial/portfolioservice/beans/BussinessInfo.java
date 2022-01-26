package com.financial.portfolioservice.beans;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BussinessInfo {

	boolean marketLeader;

	boolean identifiableMoats;

	boolean monopoly;

	// range of 0 to 10
	int addressableMarketSize;

	boolean isMarketConsolidated;

}
