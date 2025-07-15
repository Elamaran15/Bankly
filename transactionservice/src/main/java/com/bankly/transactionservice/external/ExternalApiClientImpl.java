package com.bankly.transactionservice.external;

import com.bankly.transactionservice.exception.ApiClientException;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Map;

@Service
public class ExternalApiClientImpl implements ExternalApiClient{

    private final WebClient webClient;

    public ExternalApiClientImpl(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    @Override
    public <T> T postBlocking(String url, Object requestBody, Class<T> responseType) {
        return webClient.post()
                .uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .retrieve()
                .onStatus(HttpStatusCode::isError, response ->
                        response.bodyToMono(String.class)
                                .flatMap(body -> Mono.error(new ApiClientException(
                                        "API call failed with status " + response.statusCode() + ": " + body,
                                        response.statusCode()
                                ))))
                                .bodyToMono(responseType)
                                .block(Duration.ofSeconds(5));
    }

    @Override
    public <T> Mono<T> postReactive(String url, Object requestBody, Class<T> responseType) {
        return webClient.post()
                .uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .retrieve()
                .onStatus(HttpStatusCode::isError, response ->
                        response.bodyToMono(String.class)
                                .flatMap(body -> Mono.error(new ApiClientException(
                                        "API call failed with status " + response.statusCode() + ": " + body,
                                        response.statusCode()
                                ))))
                                .bodyToMono(responseType);
    }



    public <T> T getBlocking(String url, Map<String, ?> uriVariables, Class<T> responseType) {
        return webClient.get()
                .uri(url, uriVariables)
                .retrieve()
                .onStatus(HttpStatusCode::isError, response ->
                        response.bodyToMono(String.class)
                                .flatMap(body -> Mono.error(new ApiClientException(
                                        "API call failed with status " + response.statusCode() + ": " + body,
                                        response.statusCode()
                                ))))
                .bodyToMono(responseType)
                .block(Duration.ofSeconds(5));
    }
}
