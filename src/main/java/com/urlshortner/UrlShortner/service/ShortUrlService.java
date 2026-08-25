package com.urlshortner.UrlShortner.service;
import com.urlshortner.UrlShortner.entity.ShortUrl;
import com.urlshortner.UrlShortner.exception.ShortUrlNotFoundException;
import com.urlshortner.UrlShortner.repository.ShortUrlRepo;
import com.urlshortner.UrlShortner.util.CodeGenerator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShortUrlService {
    private final ShortUrlRepo shortUrlRepo;
    private final CodeGenerator codeGenerator;

    private static final int MAX_RETRIES = 5;

    @Transactional
    public ShortUrl createShortUrl(String originalUrl, Long ownerId) {
        String shortCode = this.generateUniqueCode();

        ShortUrl shortUrl = ShortUrl.builder()
                .originalUrl(originalUrl)
                .shortCode(shortCode)
                .ownerId(ownerId)
                .build();

        return shortUrlRepo.save(shortUrl);
    }

    public String generateUniqueCode() {
        for (int i = 0; i < MAX_RETRIES; i++) {
            String candidate = codeGenerator.generate();
            if(!shortUrlRepo.existsByShortCode(candidate)) {
                return candidate;
            }
        }

        throw new IllegalStateException("Code generation failed");
    }

    @Transactional
    public ShortUrl resolveAndTrack(String shortCode) {
        ShortUrl shortUrl = shortUrlRepo.findByShortCode(shortCode)
                .orElseThrow(() -> new ShortUrlNotFoundException(shortCode));


        shortUrl.setClickCount(shortUrl.getClickCount() + 1);
        return shortUrl;
    }

    public ShortUrl getStats(String shortCode) {
        return shortUrlRepo.findByShortCode(shortCode)
                .orElseThrow(() -> new ShortUrlNotFoundException(shortCode));
    }
}