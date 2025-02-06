package com.sample.interview;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@Validated
@RequestMapping("/api")
public class APIController {
    private static final String UBS_USER_HEADER = "x-ubs-user";
    private static final Logger logger = LoggerFactory.getLogger(APIController.class);

    @GetMapping("/info")
    public Mono<ResponseEntity<Void>> infoHandler() {
        return Mono.just(ResponseEntity.ok().build());
    }

    @GetMapping("/payments")
    public Mono<ResponseEntity<Void>> paymentsHandler() {
        return Mono.just(ResponseEntity.ok().build());
    }

    /**
     * Logs info about all incoming requests. Current implementation rejects any requests
     * to the /payments endpoint with a HTTP 429 (Too Many Requests)
     * @return
     */
    @Bean
    public WebFilter requestFilter() {
        return (ServerWebExchange exchange, WebFilterChain chain) -> {
            String path = exchange.getRequest().getPath().toString();
            String ubsUser = exchange.getRequest().getHeaders().getFirst(UBS_USER_HEADER);
            logger.info(
                    "Request path: {}, x-ubs-user header value: {}, timestamp: {}",
                    path,
                    ubsUser,
                    ZonedDateTime.now(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern( "uuuu.MM.dd HH:mm:ss" ))
            );
            if (path.endsWith("/payments")) {
                exchange.getResponse().setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
                return exchange.getResponse().setComplete();
            } else {
                return chain.filter(exchange);
            }
        };
    }
}
