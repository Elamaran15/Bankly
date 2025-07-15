package com.bankly.apigatewayservice.config;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    // Removed dependency on GatewayAuthFilter as security is removed.
    // This class is now primarily for programmatic routing if needed,
    // though application.yml is also used.

    // This bean can be used to define routes programmatically.
    // The routes are already defined in application.yml, this is just for demonstration
    // of programmatic routing if needed.
    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                // All routes are now public as no security is applied at the gateway.
                .route("user-service", r -> r.path("/api/users/**")
                        .uri("lb://user-service"))
                .route("transaction-service", r -> r.path("/api/transactions/**")
                        .uri("lb://transaction-service"))
                .route("payment-service", r -> r.path("/api/payments/**")
                        .uri("lb://payment-service"))
                .build();
    }

}
