package backend.api_gateway.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import jakarta.annotation.security.PermitAll;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final WebClient webClient;

    @Value("${user.service.url}")
    private String userServiceUrl;

    public UserController(WebClient webClient) {
        this.webClient = webClient;
    }
    @PermitAll
    @PostMapping("/auth/register")
    public ResponseEntity<Map<String, Object>> registerUser(@RequestBody Object user) {
        String url = userServiceUrl + "/api/users/auth/register";
        return webClient.post()
                .uri(url)
                .bodyValue(user)
                .retrieve()
                .toEntity(new ParameterizedTypeReference<Map<String, Object>>() {})
                .block();
    }
    @PermitAll
    @PostMapping("/auth/login")
    public ResponseEntity<Map<String, Object>> loginUser(@RequestBody Object loginData) {
        String url = userServiceUrl + "/api/users/auth/login";
        return webClient.post()
                .uri(url)
                .bodyValue(loginData)
                .retrieve()
                .toEntity(new ParameterizedTypeReference<Map<String, Object>>() {})
                .block();
    }

    @GetMapping("/profile/{id}")
    public ResponseEntity<Map<String, Object>> getUserProfile(@PathVariable Long id) {
        String url = userServiceUrl + "/api/users/profile/" + id;
        return webClient.get()
                .uri(url)
                .retrieve()
                .toEntity(new ParameterizedTypeReference<Map<String, Object>>() {})
                .block();
    }

    @PostMapping("/watchlist")
    public ResponseEntity<Map<String, Object>> addToWatchlist(@RequestBody Object request) {
        String url = userServiceUrl + "/api/users/watchlist";
        return webClient.post()
                .uri(url)
                .bodyValue(request)
                .retrieve()
                .toEntity(new ParameterizedTypeReference<Map<String, Object>>() {})
                .block();
    }

    @DeleteMapping("/watchlist")
    public ResponseEntity<Map<String, Object>> removeFromWatchlist(@RequestBody Object request) {
        String url = userServiceUrl + "/api/users/watchlist";
        return webClient.method(HttpMethod.DELETE)
                .uri(url)
                .bodyValue(request)
                .retrieve()
                .toEntity(new ParameterizedTypeReference<Map<String, Object>>() {})
                .block();
    }

    @GetMapping("/watchlist/{id}")
    public ResponseEntity<Map<String, Object>> getWatchlist(@PathVariable Long id) {
        String url = userServiceUrl + "/api/users/watchlist/" + id;
        return webClient.get()
                .uri(url)
                .retrieve()
                .toEntity(new ParameterizedTypeReference<Map<String, Object>>() {})
                .block();
    }
}
