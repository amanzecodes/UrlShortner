package com.urlshortner.UrlShortner.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record CreateShortUrlRequest (
        @NotBlank
        @Pattern(regexp = "^https?://.+", message = "URL must start with http:// or https://")
        String originalUrl
){}