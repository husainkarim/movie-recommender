package backend.user_service.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import backend.user_service.dto.AuthRequest;
import backend.user_service.dto.TwoFactorCodeRequest;
import backend.user_service.dto.TwoFactorSetupRequest;
import backend.user_service.dto.TwoFactorVerifyRequest;
import backend.user_service.dto.WatchListRequest;
import backend.user_service.model.User;
import backend.user_service.repository.UserRepository;
import backend.user_service.service.JwtService;
import backend.user_service.service.TwoFactorService;
import jakarta.annotation.security.PermitAll;

@RestController
@RequestMapping("/api/users")
public class UserController {
    final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final TwoFactorService twoFactorService;
    
    private static final String MESSAGE = "message";
    private static final String USER_NOT_FOUND = "User not found";

    public UserController(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService,
        TwoFactorService twoFactorService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.twoFactorService = twoFactorService;
    }

    // POST /auth/register: Create a new user node in Neo4j (or a relational DB if preferred, 
    // but keeping users in Neo4j allows for "Users who follow users" features later).
    @PermitAll() // Allow anyone to access registration and login endpoints
    @PostMapping("/auth/register")
    public ResponseEntity<Map<String, Object>> registerUser(@RequestBody AuthRequest userRequest) {
        Map<String, Object> response = new HashMap<>();
        if (userRequest.getEmail() == null || userRequest.getEmail().isEmpty()) {
            response.put(MESSAGE, "Email cannot be empty");
            return ResponseEntity.badRequest().body(response);
        }
        if (userRepository.findByEmail(userRequest.getEmail()).isPresent()) {
            response.put(MESSAGE, "Email already in use");
            return ResponseEntity.badRequest().body(response);
        }
        if (userRequest.getPassword() == null || userRequest.getPassword().isEmpty()) {
            response.put(MESSAGE, "Password cannot be empty");
            return ResponseEntity.badRequest().body(response);
        }
        if (userRequest.getPassword().length() < 8 || userRequest.getPassword().length() > 100) {
            response.put(MESSAGE, "Password must be between 8 and 100 characters long");
            return ResponseEntity.badRequest().body(response);
        }
        User user = new User();
        user.setEmail(userRequest.getEmail());
        user.setRole("USER");
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        user.setTwoFactorEnabled(false);
        userRepository.save(user);
        response.put(MESSAGE, "User registered successfully");
        return ResponseEntity.ok().body(response);
    }

    // POST /auth/login: Issue JWT tokens.
    @PermitAll() // Allow anyone to access registration and login endpoints
    @PostMapping("/auth/login")
    public ResponseEntity<Map<String, Object>> loginUser(@RequestBody AuthRequest userRequest) {
        Map<String, Object> response = new HashMap<>();
        var existingUserOpt = userRepository.findByEmail(userRequest.getEmail());
        if (existingUserOpt.isEmpty()) {
            response.put(MESSAGE, "Invalid email or password");
            return ResponseEntity.status(401).body(response);
        }
        var existingUser = existingUserOpt.get();
        if (!passwordEncoder.matches(userRequest.getPassword(), existingUser.getPassword())) {
            response.put(MESSAGE, "Invalid email or password");
            return ResponseEntity.status(401).body(response);
        }
        // send secret to user via email or display QR code for authenticator app setup
        // for demo purposes, we'll just print it to the console and require the user to verify it on the next step
        userRepository.save(existingUser);
        userRequest.setPassword(null); // Don't return the password in the response
        response.put("requires2fa", true);
        response.put("authRequest", userRequest);
        response.put(MESSAGE, "2FA code required");
        return ResponseEntity.ok().body(response);
    }

    @PermitAll() // Allow anyone to access registration and login endpoints
    @GetMapping("/auth/2fa/qr")
    public ResponseEntity<byte[]> getQrCode(@RequestParam String email) throws Exception {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        User user = userOpt.get();
        if (!user.isTwoFactorEnabled()) {
            String secret = twoFactorService.generateSecret();
            user.setTwoFactorSecret(secret);
            user.setTwoFactorEnabled(true);
            userRepository.save(user);
        }
        String url = twoFactorService.buildOtpAuthUrl(email, user.getTwoFactorSecret());
        byte[] qr = twoFactorService.generateQrCode(url);

        return ResponseEntity.ok()
            .header("Content-Type", "image/png")
            .body(qr);
    }

    @PermitAll()
    @PostMapping("/auth/2fa/verify")
    public ResponseEntity<Map<String, Object>> verifyLoginWith2fa(@RequestBody TwoFactorVerifyRequest request) {
        Map<String, Object> response = new HashMap<>();
        var existingUserOpt = userRepository.findByEmail(request.getEmail());
        if (existingUserOpt.isEmpty()) {
            response.put(MESSAGE, "Invalid credentials or code");
            return ResponseEntity.status(401).body(response);
        }

        User existingUser = existingUserOpt.get();

        boolean isValidCode = twoFactorService.verifyCode(existingUser.getTwoFactorSecret(), request.getCode());
        if (!isValidCode) {
            response.put(MESSAGE, "Invalid 2FA code");
            return ResponseEntity.status(401).body(response);
        }

        String token = jwtService.generateToken(existingUser.getId(), existingUser.getEmail(), existingUser.getRole());
        response.put("requires2fa", false);
        response.put("token", token);
        response.put("user", existingUser);
        response.put(MESSAGE, "2FA verification successful");
        return ResponseEntity.ok().body(response);
    }

    @PermitAll
    @PostMapping("/2fa/setup")
    public ResponseEntity<Map<String, Object>> setupTwoFactor(@RequestBody TwoFactorSetupRequest request) {
        Map<String, Object> response = new HashMap<>();
        var userOpt = userRepository.findById(request.getUserId());
        if (userOpt.isEmpty()) {
            response.put(MESSAGE, USER_NOT_FOUND);
            return ResponseEntity.status(404).body(response);
        }

        var user = userOpt.get();
        String secret = twoFactorService.generateSecret();
        user.setTwoFactorSecret(secret);
        userRepository.save(user);

        response.put("secret", secret);
        response.put("otpauthUrl", twoFactorService.buildOtpAuthUrl(user.getEmail(), secret));
        response.put(MESSAGE, "2FA setup generated. Verify one code to enable 2FA.");
        return ResponseEntity.ok().body(response);
    }

    @PermitAll
    @PostMapping("/2fa/enable")
    public ResponseEntity<Map<String, Object>> enableTwoFactor(@RequestBody TwoFactorCodeRequest request) {
        Map<String, Object> response = new HashMap<>();
        var userOpt = userRepository.findById(request.getUserId());
        if (userOpt.isEmpty()) {
            response.put(MESSAGE, USER_NOT_FOUND);
            return ResponseEntity.status(404).body(response);
        }

        var user = userOpt.get();
        if (user.getTwoFactorSecret() == null || user.getTwoFactorSecret().isBlank()) {
            response.put(MESSAGE, "Run /2fa/setup before enabling 2FA");
            return ResponseEntity.badRequest().body(response);
        }

        boolean isValidCode = twoFactorService.verifyCode(user.getTwoFactorSecret(), request.getCode());
        if (!isValidCode) {
            response.put(MESSAGE, "Invalid 2FA code");
            return ResponseEntity.status(401).body(response);
        }

        userRepository.save(user);
        response.put(MESSAGE, "2FA enabled successfully");
        return ResponseEntity.ok().body(response);
    }

    @PermitAll
    @PostMapping("/2fa/disable")
    public ResponseEntity<Map<String, Object>> disableTwoFactor(@RequestBody TwoFactorCodeRequest request) {
        Map<String, Object> response = new HashMap<>();
        var userOpt = userRepository.findById(request.getUserId());
        if (userOpt.isEmpty()) {
            response.put(MESSAGE, USER_NOT_FOUND);
            return ResponseEntity.status(404).body(response);
        }

        var user = userOpt.get();
        if (user.getTwoFactorSecret() == null) {
            response.put(MESSAGE, "2FA is not enabled for this user");
            return ResponseEntity.badRequest().body(response);
        }

        boolean isValidCode = twoFactorService.verifyCode(user.getTwoFactorSecret(), request.getCode());
        if (!isValidCode) {
            response.put(MESSAGE, "Invalid 2FA code");
            return ResponseEntity.status(401).body(response);
        }

        user.setTwoFactorSecret(null);
        userRepository.save(user);
        response.put(MESSAGE, "2FA disabled successfully");
        return ResponseEntity.ok().body(response);
    }
    

    // GET /users/profile
    // get movies rated by the user
    @PermitAll
    @GetMapping("/profile/{id}")
    public ResponseEntity<Map<String, Object>> getUserProfile(@PathVariable String id) {
        Map<String, Object> response = new HashMap<>();
        var userOpt = userRepository.findById(id);
        if (userOpt.isEmpty()) {
            response.put(MESSAGE, USER_NOT_FOUND);
            return ResponseEntity.status(404).body(response);
        }
        var user = userOpt.get();
        response.put("user", user);
        response.put(MESSAGE, "User profile retrieved successfully");
        return ResponseEntity.ok().body(response);
    }

    // add movie to watchlist
    // POST /users/watchlist
    @PermitAll
    @PostMapping("/watchlist")
    public ResponseEntity<Map<String, Object>> addToWatchlist(@RequestBody WatchListRequest request) {
        Map<String, Object> response = new HashMap<>();
        var userOpt = userRepository.findById(request.getUserId());
        if (userOpt.isEmpty()) {
            response.put(MESSAGE, USER_NOT_FOUND);
            return ResponseEntity.status(404).body(response);
        }
        userRepository.addToWatchlist(userOpt.get().getId(), request.getMovieId());
        response.put(MESSAGE, "Movie added to watchlist");
        return ResponseEntity.ok().body(response);
    }

    // remove movie from watchlist
    // DELETE /users/watchlist
    @PermitAll
    @DeleteMapping("/watchlist")
    public ResponseEntity<Map<String, Object>> removeFromWatchlist(@RequestBody WatchListRequest request) {
        Map<String, Object> response = new HashMap<>();
        var userOpt = userRepository.findById(request.getUserId());
        if (userOpt.isEmpty()) {
            response.put(MESSAGE, USER_NOT_FOUND);
            return ResponseEntity.status(404).body(response);
        }
        userRepository.removeFromWatchlist(userOpt.get().getId(), request.getMovieId());
        response.put(MESSAGE, "Movie removed from watchlist");
        return ResponseEntity.ok().body(response);
    }
}
