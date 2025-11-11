package com.infosysSpringboard.EcoBazarX.controller;


import com.infosysSpringboard.EcoBazarX.model.UserProfile;
import com.infosysSpringboard.EcoBazarX.repo.UserProfileRepo;
import com.infosysSpringboard.EcoBazarX.service.LeaderboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/leaderboard")
public class LeaderboardController {

    @Autowired
    private UserProfileRepo userProfileRepo;


    @Autowired
    private LeaderboardService leaderboardService;

    @GetMapping("/top")
    public ResponseEntity<List<UserProfile>> getTopEcoUsers() {
        return ResponseEntity.ok(leaderboardService.getLeaderboard());
    }

    @GetMapping("/rank/{userId}")
    public ResponseEntity<?> getUserRank(@PathVariable Long userId) {
        return ResponseEntity.ok(leaderboardService.getUserRank(userId));
    }


    @GetMapping("/top-points")
    public ResponseEntity<List<UserProfile>> getTopByCarbonPoints() {
        List<UserProfile> topUsers = userProfileRepo.findTop10ByOrderByCarbonPointsDesc();
        return ResponseEntity.ok(topUsers);
    }

    @GetMapping("/top-carbon")
    public ResponseEntity<List<UserProfile>> getTopByCarbonSaved() {
        List<UserProfile> topUsers = userProfileRepo.findTop10ByOrderByCarbonSavedDesc();
        return ResponseEntity.ok(topUsers);
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<?> getUserStats(@PathVariable String username) {
        return ResponseEntity.ok(userProfileRepo.findProfileWithRank(username));
    }
}
