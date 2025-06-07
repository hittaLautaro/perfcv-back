package com.hitta.AuthTemplate.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class VerificationToken {
    @Id
    private String token;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
    private LocalDateTime lastSentAt;

    public VerificationToken(Users user, int expirationMinutes) {
        this.token = UUID.randomUUID().toString();
        this.user = user;
        this.createdAt = LocalDateTime.now();
        this.expiresAt = this.createdAt.plusMinutes(expirationMinutes);
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(this.expiresAt);
    }
}