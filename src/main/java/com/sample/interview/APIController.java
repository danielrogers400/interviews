package com.sample.interview;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@RestController
@Validated
@Slf4j
@RequestMapping("/api")
public class APIController {

    @GetMapping("/info")
    public Mono<ServerResponse> infoHandler() {
        return ServerResponse.ok().build();
    }

    @GetMapping("/payments")
    public Mono<ServerResponse> paymentsHandler() {
        return ServerResponse.ok().build();
    }

    @Bean
    public WebFilter requestFilter() {
        return (ServerWebExchange exchange, WebFilterChain chain) -> {
            String path = exchange.getRequest().getPath().toString();
            if (path.endsWith("/payments")) {
                exchange.getResponse().setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
                return exchange.getResponse().setComplete();
            } else {
                return chain.filter(exchange);
            }
        };
    }
}
