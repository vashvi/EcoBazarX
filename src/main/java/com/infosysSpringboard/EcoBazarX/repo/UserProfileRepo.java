package com.infosysSpringboard.EcoBazarX.repo;


import com.infosysSpringboard.EcoBazarX.model.UserProfile;
import com.infosysSpringboard.EcoBazarX.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserProfileRepo extends JpaRepository<UserProfile, Long> {
    Optional<UserProfile> findByUser(Users user);

    List<UserProfile> findTop10ByOrderByCarbonPointsDesc();

    List<UserProfile> findTop10ByOrderByCarbonSavedDesc();

    @Query("""
        SELECT CONCAT(
            'User: ', u.username, 
            ', Points: ', up.carbonPoints, 
            ', Rank: ', (
                SELECT COUNT(*) + 1 FROM UserProfile x WHERE x.carbonPoints > up.carbonPoints
            )
        )
        FROM UserProfile up 
        JOIN up.user u 
        WHERE u.username = :username
    """)
    String findProfileWithRank(String username);
}
