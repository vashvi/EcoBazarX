package com.infosysSpringboard.EcoBazarX.repo;


import com.infosysSpringboard.EcoBazarX.model.CarbonInsight;
import com.infosysSpringboard.EcoBazarX.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CarbonInsightRepository extends JpaRepository<CarbonInsight, Long> {
    List<CarbonInsight> findByUserOrderByRecordedAtDesc(Users user);
    Optional<CarbonInsight> findByUser(Users user);

}
