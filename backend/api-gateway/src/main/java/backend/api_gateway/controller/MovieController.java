package backend.api_gateway.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

@RestController
@RequestMapping("/api/movies")
public class MovieController {
    private final WebClient webClient;

    @Value("${movie.service.url}")
    private String movieServiceUrl;

    public MovieController(WebClient webClient) {
        this.webClient = webClient;
    }

    @GetMapping("")
    public ResponseEntity<Map<String, Object>> getListMovies(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        String url = movieServiceUrl + "/api/movies?page=" + page + "&size=" + size;
        return webClient.get()
                .uri(url)
                .retrieve()
                .toEntity(new ParameterizedTypeReference<Map<String, Object>>() {})
                .block();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getMovieById(@PathVariable Long id) {
        String url = movieServiceUrl + "/api/movies/" + id;
        return webClient.get()
                .uri(url)
                .retrieve()
                .toEntity(new ParameterizedTypeReference<Map<String, Object>>() {})
                .block();
    }

    @PostMapping("/")
    public ResponseEntity<Map<String, Object>> createMovie(@RequestBody Object movieData) {
        String url = movieServiceUrl + "/api/movies";
        return webClient.post()
                .uri(url)
                .bodyValue(movieData)
                .retrieve()
                .toEntity(new ParameterizedTypeReference<Map<String, Object>>() {})
                .block();
    }

    @GetMapping("/persons/{id}")
    public ResponseEntity<Map<String, Object>> getPersonById(@PathVariable Long id) {
        String url = movieServiceUrl + "/api/movies/persons/" + id;
        return webClient.get()
                .uri(url)
                .retrieve()
                .toEntity(new ParameterizedTypeReference<Map<String, Object>>() {})
                .block();
    }
    
}
