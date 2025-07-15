package com.bankly.transactionservice.external;

import reactor.core.publisher.Mono;

import java.util.Map;

public interface ExternalApiClient {

    <T> T postBlocking(String url, Object requestBody, Class<T> responseType);
    <T> Mono<T> postReactive(String url, Object requestBody, Class<T> responseType);
    public <T> T getBlocking(String url, Map<String, ?> uriVariables , Class<T> responseType);

}
