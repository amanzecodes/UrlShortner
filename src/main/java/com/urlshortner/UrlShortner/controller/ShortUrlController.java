package com.urlshortner.UrlShortner.controller;
import com.urlshortner.UrlShortner.entity.ShortUrl;
import com.urlshortner.UrlShortner.repository.ShortUrlRepo;
import com.urlshortner.UrlShortner.service.ShortUrlService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ShortUrlController {
    private final ShortUrlRepo shortUrlRepo;

    public ShortUrl createShortUrl(@RequestBody ShortUrl shortUrl) {
        return null;
    }
}
