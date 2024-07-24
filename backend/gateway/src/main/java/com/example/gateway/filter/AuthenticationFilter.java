package com.example.gateway.filter;



import com.example.gateway.config.Carousel;
import com.example.gateway.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    private final RouteValidator validator;
    private final RestTemplate template;
    private final JwtUtil jwtUtil;
    private final Carousel carousel;

    @Value("${spring.profiles.active}")
    private String activeProfile;

    public AuthenticationFilter(RouteValidator validator, RestTemplate template, JwtUtil jwtUtil, Carousel carousel) {
        super(Config.class);
        this.validator = validator;
        this.template = template;
        this.jwtUtil = jwtUtil;
        this.carousel = carousel;


    }
    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            if (validator.isSecure.test(exchange.getRequest())) {
                if (!exchange.getRequest().getCookies().containsKey(HttpHeaders.AUTHORIZATION) &&
                        !exchange.getRequest().getCookies().containsKey("refresh")) {
                    return prepareUnauthorizedResponse(exchange);
                }

                HttpCookie authCookie = exchange.getRequest().getCookies().getFirst(HttpHeaders.AUTHORIZATION);
                HttpCookie refreshCookie = exchange.getRequest().getCookies().getFirst("refresh");

                try {
                    if (activeProfile.equals("test")) {
                        jwtUtil.validateToken(authCookie.getValue());
                    } else {
                        String cookies = authCookie.getName() + "=" + authCookie.getValue() + ":" +
                                refreshCookie.getName() + "=" + refreshCookie.getValue();

                        HttpHeaders httpHeaders = new HttpHeaders();
                        httpHeaders.add(HttpHeaders.COOKIE, cookies);
                        HttpEntity<Object> entity = new HttpEntity<>(httpHeaders);

                        ResponseEntity<String> response = template.exchange("http://" + carousel.getUriAuth() +
                                "/api/v1/auth/validate", HttpMethod.GET, entity, String.class);

                        if (response.getStatusCode().equals(HttpStatus.OK)) {
                            List<String> cookieList = response.getHeaders().get(HttpHeaders.SET_COOKIE);
                            if (cookieList != null && !cookieList.isEmpty()) {
                                java.net.HttpCookie.parse(cookieList.get(0))
                                        .forEach(cookie -> exchange.getResponse().addCookie(
                                                ResponseCookie.from(cookie.getName(), cookie.getValue())
                                                        .domain(cookie.getDomain())
                                                        .path(cookie.getPath())
                                                        .maxAge(cookie.getMaxAge())
                                                        .secure(cookie.getSecure())
                                                        .httpOnly(cookie.isHttpOnly())
                                                        .build()));
                            }
                        }
                    }
                } catch (Exception e) {
                    return prepareErrorResponse(exchange, e);
                }
            }
            return chain.filter(exchange);
        };
    }


    private Mono<Void> prepareUnauthorizedResponse(ServerWebExchange exchange) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

        String errorBody = "{\n" +
                "  \"timestamp\": \"" + System.currentTimeMillis() + "\",\n" +
                "  \"message\": \"Wskazany token jest pusty lub nie ważny\",\n" +
                "  \"code\": \"A3\"\n" +
                "}";
        return exchange.getResponse().writeWith(Mono.just(new DefaultDataBufferFactory().wrap(errorBody.getBytes())));
    }

    private Mono<Void> prepareErrorResponse(ServerWebExchange exchange, Exception exception) {
        exchange.getResponse().setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        exchange.getResponse().getHeaders().setContentType(MediaType.TEXT_PLAIN);

        String errorMessage = "Błąd wewnętrzny serwera: " + exception.getMessage();
        return exchange.getResponse().writeWith(Flux.just(new DefaultDataBufferFactory().wrap(errorMessage.getBytes())));
    }

    public static class Config {

    }
}

