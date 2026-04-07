package backend.api_gateway.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

@RestController
@RequestMapping("/api/recommendations")
public class RecommendationController {
    private final WebClient webClient;

    @Value("${recommendation.service.url}")
    private String recommendationServiceUrl;

    public RecommendationController(WebClient webClient) {
        this.webClient = webClient;
    }

    @GetMapping("/collaborative/{userId}")
    public ResponseEntity<Map<String, Object>> getCollaborativeRecommendations(@PathVariable String userId) {
        String url = recommendationServiceUrl + "/api/recommendations/collaborative/" + userId;
        return webClient.get()
                .uri(url)
                .retrieve()
                .toEntity(new ParameterizedTypeReference<Map<String, Object>>() {})
                .block();
    }

    @GetMapping("/content/{userId}")
    public ResponseEntity<Map<String, Object>> getContentBasedRecommendations(@PathVariable String userId) {
        String url = recommendationServiceUrl + "/api/recommendations/content/" + userId;
        return webClient.get()
                .uri(url)
                .retrieve()
                .toEntity(new ParameterizedTypeReference<Map<String, Object>>() {})
                .block();
    }

    @GetMapping("/similar/{movieId}")
    public ResponseEntity<Map<String, Object>> getSimilarMovies(@PathVariable Long movieId) {
        String url = recommendationServiceUrl + "/api/recommendations/similar/" + movieId;
        return webClient.get()
                .uri(url)
                .retrieve()
                .toEntity(new ParameterizedTypeReference<Map<String, Object>>() {})
                .block();
    }
}
