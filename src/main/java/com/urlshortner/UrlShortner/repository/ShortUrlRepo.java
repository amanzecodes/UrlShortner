package com.urlshortner.UrlShortner.repository;

import com.urlshortner.UrlShortner.entity.ShortUrl;
import com.urlshortner.UrlShortner.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShortUrlRepo extends JpaRepository<ShortUrl, Long> {
    Optional<ShortUrl> findByShortCode(String shortCode);
    boolean existsByShortCode(String shortCode);
    List<ShortUrl> findByOwner(Users owner);
}