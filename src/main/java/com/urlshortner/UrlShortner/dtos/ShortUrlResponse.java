package com.urlshortner.UrlShortner.dtos;

import com.urlshortner.UrlShortner.entity.ShortUrl;
import java.time.Instant;

public record ShortUrlResponse(
        String shortCode,
        String originalUrl,
        Long clickCount,
        Instant createdAt
) {
    public static ShortUrlResponse from(ShortUrl shortUrl) {
        return new ShortUrlResponse(
                shortUrl.getShortCode(),
                shortUrl.getOriginalUrl(),
                shortUrl.getClickCount(),
                shortUrl.getCreatedAt()
        );
    }
}