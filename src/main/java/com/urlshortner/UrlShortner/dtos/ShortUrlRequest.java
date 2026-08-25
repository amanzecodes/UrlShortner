package com.urlshortner.UrlShortner.dtos;
import jakarta.validation.constraints.NotNull;

public class ShortUrlRequest {
    @NotNull
    private String originalUrl;
}