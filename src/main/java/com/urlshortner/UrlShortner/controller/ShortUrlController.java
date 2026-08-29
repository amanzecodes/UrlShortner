package com.urlshortner.UrlShortner.controller;
import com.urlshortner.UrlShortner.dtos.CreateShortUrlRequest;
import com.urlshortner.UrlShortner.dtos.ShortUrlResponse;
import com.urlshortner.UrlShortner.entity.ShortUrl;
import com.urlshortner.UrlShortner.entity.Users;
import com.urlshortner.UrlShortner.repository.ShortUrlRepo;
import com.urlshortner.UrlShortner.repository.UserRepo;
import com.urlshortner.UrlShortner.service.ShortUrlService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ShortUrlController {
    private final ShortUrlRepo shortUrlRepo;
    private final ShortUrlService shortUrlService;
    private final UserRepo userRepo;

    @PostMapping("/api/urls")
    public ResponseEntity<ShortUrlResponse> create(
            @Valid @RequestBody CreateShortUrlRequest request,
            Authentication authentication
    ) {
        Users owner = null;
        if (authentication != null && authentication.isAuthenticated()) {
            String email = authentication.getName();
            owner = userRepo.findByEmail(email)
                    .orElse(null);
        }
        ShortUrl created = shortUrlService.createShortUrl(request.originalUrl(), owner);
        return ResponseEntity.status(HttpStatus.CREATED).body(ShortUrlResponse.from(created));
    }

    @GetMapping("/r/{code}")
    public ResponseEntity<Void> redirect(@PathVariable String code) {
        ShortUrl shortUrl = shortUrlService.resolveAndTrack(code);
        return ResponseEntity.status(HttpStatus.FOUND)
                .header(HttpHeaders.LOCATION, shortUrl.getOriginalUrl())
                .build();
    }

    @GetMapping("/api/urls/{code}/stats")
    public ResponseEntity<ShortUrlResponse> stats(@PathVariable String code) {
        return ResponseEntity.ok(ShortUrlResponse.from(shortUrlService.getStats(code)));
    }

    @GetMapping("/api/urls/mine")
    public ResponseEntity<List<ShortUrlResponse>> getMyUrls(Authentication authentication) {
        String email = authentication.getName();
        Users user = userRepo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("No user found with email: " + email));

        List<ShortUrlResponse> urls = shortUrlService.getUrlsForUser(user)
                .stream()
                .map(ShortUrlResponse::from)
                .toList();

        return ResponseEntity.ok(urls);
    }
}