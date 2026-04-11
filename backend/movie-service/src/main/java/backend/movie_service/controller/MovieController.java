package backend.movie_service.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import backend.movie_service.model.Movie;
import backend.movie_service.model.Person;
import backend.movie_service.repository.MovieRepository;
import backend.movie_service.repository.PersonRepository;

@RestController
@RequestMapping("/api/movies")
public class MovieController {
    private final MovieRepository movieRepository;
    private final PersonRepository personRepository;
    private static final String MESSAGE = "message";

    public MovieController(MovieRepository movieRepository, PersonRepository personRepository) {
        this.movieRepository = movieRepository;
        this.personRepository = personRepository;
    }

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
            response.put(MESSAGE, "Movies retrieved successfully");
            
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put(MESSAGE, "Error retrieving movies: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // GET /movies/{id}: Get movie details including its genre and cast.
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getMovieDetails(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Movie movie = movieRepository.findById(id).orElse(null);
            if (movie != null) {
                response.put("movie", movie);
                response.put(MESSAGE, "Movie details retrieved successfully");
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                response.put(MESSAGE, "Movie not found with id: " + id);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            response.put(MESSAGE, "Error retrieving movie details: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    // POST /movies: Add new movies to Neo4j.
    @PostMapping("")
    public ResponseEntity<Map<String, Object>> addMovie(@RequestBody Movie movie) {
        Map<String, Object> response = new HashMap<>();
        try {
            Movie savedMovie = movieRepository.save(movie);
            response.put("movie", savedMovie);
            response.put(MESSAGE, "Movie added successfully");
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            response.put(MESSAGE, "Error adding movie: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // GET /persons/{id}: Get actor/director details and their filmography.
    @GetMapping("/persons/{id}")
    public ResponseEntity<Map<String, Object>> getPersonDetails(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Person person = personRepository.findById(id).orElse(null);
            if (person != null) {
                response.put("person", person);
                response.put(MESSAGE, "Person details retrieved successfully");
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                response.put(MESSAGE, "Person not found with id: " + id);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            response.put(MESSAGE, "Error retrieving person details: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
