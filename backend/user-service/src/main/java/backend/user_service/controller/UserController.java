package backend.user_service.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import backend.user_service.model.User;
import backend.user_service.repository.UserRepository;
import backend.user_service.service.JwtService;


@RestController
@RequestMapping("/api/users")
public class UserController {
    final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    
    private static final String MESSAGE = "message";

    public UserController(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    // POST /auth/register: Create a new user node in Neo4j (or a relational DB if preferred, 
    // but keeping users in Neo4j allows for "Users who follow users" features later).
    @PostMapping("/auth/register")
    public ResponseEntity<Map<String, Object>> registerUser(@RequestBody User user) {
        Map<String, Object> response = new HashMap<>();
        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            response.put(MESSAGE, "Email cannot be empty");
            return ResponseEntity.badRequest().body(response);
        }
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            response.put(MESSAGE, "Email already in use");
            return ResponseEntity.badRequest().body(response);
        }
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            response.put(MESSAGE, "Password cannot be empty");
            return ResponseEntity.badRequest().body(response);
        }
        if (user.getPassword().length() < 6) {
            response.put(MESSAGE, "Password must be at least 6 characters long");
            return ResponseEntity.badRequest().body(response);
        }
        if (user.getRole() != null && !user.getRole().isEmpty() && !user.getRole().equals("USER") && !user.getRole().equals("ADMIN")) {
            response.put(MESSAGE, "Invalid role. Allowed values are 'USER' or 'ADMIN'");
            return ResponseEntity.badRequest().body(response);
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        response.put(MESSAGE, "User registered successfully");
        return ResponseEntity.ok().body(response);
    }

    // POST /auth/login: Issue JWT tokens.
    @PostMapping("/auth/login")
    public ResponseEntity<Map<String, Object>> loginUser(@RequestBody User user) {
        Map<String, Object> response = new HashMap<>();
        var existingUserOpt = userRepository.findByEmail(user.getEmail());
        if (existingUserOpt.isEmpty()) {
            response.put(MESSAGE, "Invalid email or password");
            return ResponseEntity.status(401).body(response);
        }
        var existingUser = existingUserOpt.get();
        if (!passwordEncoder.matches(user.getPassword(), existingUser.getPassword())) {
            response.put(MESSAGE, "Invalid email or password");
            return ResponseEntity.status(401).body(response);
        }
        String token = jwtService.generateToken(existingUser.getId(), existingUser.getEmail(), existingUser.getRole());
        response.put("token", token);
        response.put(MESSAGE, "Login successful");
        return ResponseEntity.ok().body(response);
    }
    

    // GET /users/profile
    // get movies rated by the user
    @GetMapping("/profile/{id}")
    public ResponseEntity<Map<String, Object>> getUserProfile(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        var userOpt = userRepository.findById(id);
        if (userOpt.isEmpty()) {
            response.put(MESSAGE, "User not found");
            return ResponseEntity.status(404).body(response);
        }
        var user = userOpt.get();
        response.put("user", user);
        response.put(MESSAGE, "User profile retrieved successfully");
        return ResponseEntity.ok().body(response);
    }
}
