package com.infosysSpringboard.EcoBazarX.model;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_profiles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private Users user;

    @Column(name = "carbon_points", nullable = false)
    @Builder.Default
    private Integer carbonPoints = 0;

    @Column(name = "carbon_saved", nullable = false)
    @Builder.Default
    private Double carbonSaved = 0.0;

    @Column(name = "eco_badge", length = 50)
    private String badge;
}
