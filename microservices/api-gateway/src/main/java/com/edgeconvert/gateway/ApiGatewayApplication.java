package com.edgeconvert.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ApiGatewayApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
    }
    
    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
            .route("diagram-parser", r -> r.path("/api/diagrams/**")
                .uri("http://diagram-parser-service:8081"))
            .route("table-management", r -> r.path("/api/tables/**")
                .uri("http://table-management-service:8082"))
            .route("relationship-management", r -> r.path("/api/relationships/**")
                .uri("http://relationship-management-service:8083"))
            .route("ddl-generation", r -> r.path("/api/ddl/**")
                .uri("http://ddl-generation-service:8084"))
            .build();
    }
}