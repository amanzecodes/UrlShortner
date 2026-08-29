package com.urlshortner.UrlShortner.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "short_url")
@Builder
@Data
@Getter
@Setter
public class ShortUrl {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 2048)
    private String originalUrl;

    @Column(nullable = false, unique = true, length = 16)
    private String shortCode;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @Column(nullable = false, updatable = false)
    private Instant updatedAt;

    @Column(nullable = false)
    @Builder.Default
    private Long clickCount = 0L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private Users owner;

    @PrePersist
    public void prePersist() {
        if(this.createdAt == null) {
            this.createdAt = Instant.now();
        }
        if(this.updatedAt == null) {
            this.updatedAt = Instant.now();
        }
    }
}