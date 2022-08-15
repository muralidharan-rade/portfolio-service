package com.financial.portfolioservice.resource;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.financial.portfolioservice.beans.Portfolio;
import com.financial.portfolioservice.service.AllocationService;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/portfolio")
public class PortfolioResource {

	@Autowired
	AllocationService allocationService;

	@PostMapping(consumes = "application/json", produces = "application/json", path = "/create")
	public Portfolio createPortfolio(@RequestBody @Valid Portfolio request) {

		return allocationService.allocatePortfolio(request);
	}



}
