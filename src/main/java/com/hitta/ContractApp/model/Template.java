package com.hitta.ContractApp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "templates")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Template {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Template name is required")
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ElementCollection
    @CollectionTable(name = "template_categories", joinColumns = @JoinColumn(name = "template_id"))
    @Column(name = "category")
    private List<String> categories;

    @NotBlank(message = "Preview image S3 key is required")
    private String previewImageS3Key;

    @NotBlank(message = "Template PDF S3 key is required")
    private String templatePdfS3Key;

    @NotBlank(message = "Template DOCX S3 key is required")
    private String templateDocxS3Key;

    @NotNull
    @Builder.Default
    private Boolean isPremium = false;

    private BigDecimal price;

    @Builder.Default
    private Integer downloadCount = 0;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private Boolean isActive = true;
}
