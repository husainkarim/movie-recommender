package backend.recommendation_service.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import backend.recommendation_service.repository.MovieRepository;


@RestController
@RequestMapping("/api/recommendations")
public class RecommendationController {
    private final MovieRepository movieRepository;

    private static final String RECOMMENDATIONS = "recommendations";

    public RecommendationController(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    // get the top 5 recommendations for a user based on collaborative filtering
    // this is the "Users who liked this also liked..." logic
    // it finds users who have rated the same movies as you and then looks at what other
    // movies those users have rated highly that you haven't seen yet.
    @GetMapping("/collaborative/{userId}")
    public ResponseEntity<Map<String, Object>> getCollaborativeRecommendations(@PathVariable String userId) {
        Map<String, Object> response = new HashMap<>();
        // check if user exists - if not, return 400
        var recommendations = movieRepository.getCollaborativeRecommendations(userId);
        if (recommendations.isEmpty()) {
            recommendations = movieRepository.getTopRatedMovies(); // fallback to top rated movies if no recommendations found
        }
        response.put(RECOMMENDATIONS, recommendations);
        return ResponseEntity.ok().body(response);
    }
    
    // get the top 5 recommendations for a user based on content-based filtering
    // this is the "Because you liked Inception..." logic
    // it looks at the attributes of movies you rated highly (like Genres or Directors)
    // and finds other movies with those same attributes.
    @GetMapping("/content/{userId}")
    public ResponseEntity<Map<String, Object>> getContentBasedRecommendations(@PathVariable String userId) {
        Map<String, Object> response = new HashMap<>();
        var recommendations = movieRepository.getContentBasedRecommendations(userId);
        if (recommendations.isEmpty()) {
            recommendations = movieRepository.getTopRatedMovies(); // fallback to top rated movies if no recommendations found
        }
        response.put(RECOMMENDATIONS, recommendations);
        return ResponseEntity.ok().body(response);
    }
    // get the top 5 movies similar to a given movie
    // this is useful for the GET /similar/{movieId} endpoint.
    // It finds movies that are frequently rated highly by the same group of users.
    @GetMapping("/similar/{movieId}")
    public ResponseEntity<Map<String, Object>> getSimilarMovies(@PathVariable Long movieId) {
        Map<String, Object> response = new HashMap<>();
        var recommendations = movieRepository.getSimilarMovies(movieId);
        if (recommendations.isEmpty()) {
            recommendations = movieRepository.getTopRatedMovies(); // fallback to top rated movies if no recommendations found
        }
        response.put(RECOMMENDATIONS, recommendations);
        return ResponseEntity.ok().body(response);
    }
}
