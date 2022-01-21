package com.financial.portfolioservice.resource;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.financial.portfolioservice.beans.Portfolio;
import com.financial.portfolioservice.service.AllocationService;

@RestController
public class CreateResource {

	@Autowired
	AllocationService allocationService;

	@PostMapping(consumes = "application/json", produces = "application/json", path = "/create")
	public Portfolio createPortfolio(@RequestBody @Valid Portfolio request) {

		return allocationService.allocatePortfolio(request);
	}

}
