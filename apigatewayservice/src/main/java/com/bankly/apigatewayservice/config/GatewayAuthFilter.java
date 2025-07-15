package com.bankly.apigatewayservice.config;


import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;

import org.springframework.stereotype.Component;


@Component
public class GatewayAuthFilter extends AbstractGatewayFilterFactory<GatewayAuthFilter.Config> {

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            // Log for demonstration that the filter is still active, but not performing security.
            System.out.println("GatewayAuthFilter: Passing request through without security checks for path: " + exchange.getRequest().getURI().getPath());
            return chain.filter(exchange);
        };
    }

    public static class Config {
        // No specific config properties needed for this pass-through filter
    }
}