package backend.api_gateway.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

@RestController
@RequestMapping("/api/ratings")
public class RatingController {
    private final WebClient webClient;

    @Value("${rating.service.url}")
    private String ratingServiceUrl;

    public RatingController(WebClient webClient) {
        this.webClient = webClient;
    }

    @PostMapping("")
    public ResponseEntity<Map<String, Object>> createRating(@RequestBody Object ratingRequest) {
        String url = ratingServiceUrl + "/api/ratings";
        return webClient.post()
                .uri(url)
                .bodyValue(ratingRequest)
                .retrieve()
                .toEntity(new ParameterizedTypeReference<Map<String, Object>>() {})
                .block();
    }

    @DeleteMapping("")
    public ResponseEntity<Map<String, Object>> deleteRating(@RequestBody Object ratingRequest) {
        String url = ratingServiceUrl + "/api/ratings";
        return webClient.method(HttpMethod.DELETE)
                .uri(url)
                .bodyValue(ratingRequest)
                .retrieve()
                .toEntity(new ParameterizedTypeReference<Map<String, Object>>() {})
                .block();
    }
}
