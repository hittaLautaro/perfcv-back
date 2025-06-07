package com.hitta.AuthTemplate.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "tokens")
@NoArgsConstructor
@Builder
@AllArgsConstructor
@ToString

public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tokens_seq")
    @SequenceGenerator(name = "tokens_seq", sequenceName = "tokens_seq", allocationSize = 1)
    private Integer id;
    private String token;

    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
    private LocalDateTime validatedAt;
    private boolean revoked;
    @JsonBackReference
    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private Users user;
}
