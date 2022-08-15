package com.financial.portfolioservice.resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping(value = "/health")
public class HealthCheckResource {

    @GetMapping(produces = "application/json")
    public Map<String, String> sampleTest() {

        return Collections.singletonMap("status", "healthy");
    }
    
}
