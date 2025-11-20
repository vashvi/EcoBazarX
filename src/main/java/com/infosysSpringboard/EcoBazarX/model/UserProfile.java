package com.infosysSpringboard.EcoBazarX.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
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
