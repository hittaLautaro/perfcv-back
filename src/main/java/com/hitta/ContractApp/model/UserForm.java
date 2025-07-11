package com.hitta.ContractApp.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.hitta.ContractApp.dtos.CvFormDto;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "user_forms")
public class UserForm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "json")
    private CvFormDto form;

    @JsonBackReference
    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private Users user;
}