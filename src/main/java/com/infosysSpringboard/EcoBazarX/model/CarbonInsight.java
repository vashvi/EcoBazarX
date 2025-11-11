package com.infosysSpringboard.EcoBazarX.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "carbon_insights")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CarbonInsight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    private BigDecimal totalCarbon;

    private Integer orderCount;

    @Column(name = "recorded_at", nullable = false)
    @Builder.Default
    private LocalDateTime recordedAt = LocalDateTime.now();
}
