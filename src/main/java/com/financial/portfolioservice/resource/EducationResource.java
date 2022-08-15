package com.financial.portfolioservice.resource;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.financial.portfolioservice.beans.EducationPlan;
import com.financial.portfolioservice.service.EducationService;

@RestController
@RequestMapping(value = "/education")
public class EducationResource {

	@Autowired
	EducationService educationService;

	@PostMapping(path = "/plan", consumes = "application/json", produces = "application/json")
	public EducationPlan createPlan(@RequestBody @Valid EducationPlan educationPlan) {

		return educationService.createPlan(educationPlan);
	}

}
