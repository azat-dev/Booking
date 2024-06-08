package com.azat4dev.booking.gateway;

import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.factory.DedupeResponseHeaderGatewayFilterFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FiltersConfig {

    @Bean
    GlobalFilter dedupeCorsFilter() {
        final var factory = new DedupeResponseHeaderGatewayFilterFactory();

        final var filter = factory.apply(new DedupeResponseHeaderGatewayFilterFactory.Config() {
            @Override
            public String getName() {
                return "Access-Control-Allow-Origin Access-Control-Allow-Credentials";
            }

            @Override
            public DedupeResponseHeaderGatewayFilterFactory.Strategy getStrategy() {
                return DedupeResponseHeaderGatewayFilterFactory.Strategy.RETAIN_LAST;
            }
        });

        return filter::filter;
    }

    @Bean
    GlobalFilter dedupeVaryFilter() {
        final var factory = new DedupeResponseHeaderGatewayFilterFactory();

        final var filter = factory.apply(new DedupeResponseHeaderGatewayFilterFactory.Config() {
            @Override
            public String getName() {
                return "Vary";
            }

            @Override
            public DedupeResponseHeaderGatewayFilterFactory.Strategy getStrategy() {
                return DedupeResponseHeaderGatewayFilterFactory.Strategy.RETAIN_UNIQUE;
            }
        });

        return filter::filter;
    }
}
