package backend.movie_service.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import backend.movie_service.model.Movie;
import backend.movie_service.repository.MovieRepository;

import org.springframework.data.domain.Pageable;


@RestController
@RequestMapping("/api/movies")
public class MovieController {
    private final MovieRepository movieRepository;
    public MovieController(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }
    // Define REST endpoints for movie-related operations:

    // GET /movies: List movies (with pagination).
    @GetMapping("")
    public ResponseEntity<Map<String, Object>> getListMovies(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Map<String, Object> response = new HashMap<>();
        try {
            // Create a Pageable object
            Pageable pageable = PageRequest.of(page, size);
            
            // Fetch the page of movies
            Page<Movie> moviePage = movieRepository.findAll(pageable);

            response.put("movies", moviePage.getContent());
            response.put("currentPage", moviePage.getNumber());
            response.put("totalItems", moviePage.getTotalElements());
            response.put("totalPages", moviePage.getTotalPages());
            response.put("message", "Movies retrieved successfully");
            
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("message", "Error retrieving movies: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // GET /movies/{id}: Get movie details including its genre and cast.
    // POST /movies: Add new movies to Neo4j.
    // GET /persons/{id}: Get actor/director details and their filmography.
}
