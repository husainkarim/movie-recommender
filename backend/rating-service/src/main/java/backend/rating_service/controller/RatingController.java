package backend.rating_service.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import backend.rating_service.dto.RatingRequest;
import backend.rating_service.repository.UserRepository;


@RestController
@RequestMapping("/api/ratings")
public class RatingController {
    final UserRepository userRepository;
    final static String MESSAGE = "message";
    public RatingController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // create/update endpoints for rating movies
    @PostMapping("")
    public ResponseEntity<Map<String, Object>> createRating(@RequestBody RatingRequest ratingRequest) {
        Map<String, Object> response = new HashMap<>();
        var userOpt = userRepository.findById(ratingRequest.getUserId());
        if (userOpt.isEmpty()) {
            response.put(MESSAGE, "User not found");
            return ResponseEntity.badRequest().body(response);
        }
        userRepository.addRating(ratingRequest.getUserId(), ratingRequest.getMovieId(), ratingRequest.getRating());
        response.put(MESSAGE, "Rating added successfully");
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("")
    public ResponseEntity<Map<String, Object>> deleteRating(@RequestBody RatingRequest ratingRequest) {
        Map<String, Object> response = new HashMap<>();
        var userOpt = userRepository.findById(ratingRequest.getUserId());
        if (userOpt.isEmpty()) {
            response.put(MESSAGE, "User not found");
            return ResponseEntity.badRequest().body(response);
        }
        userRepository.deleteRating(ratingRequest.getUserId(), ratingRequest.getMovieId());
        response.put(MESSAGE, "Rating deleted successfully");
        return ResponseEntity.ok().body(response);
    }
}
