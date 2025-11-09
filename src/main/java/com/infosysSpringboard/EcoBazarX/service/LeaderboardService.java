package com.infosysSpringboard.EcoBazarX.service;

import com.infosysSpringboard.EcoBazarX.model.UserProfile;
import com.infosysSpringboard.EcoBazarX.repo.UserProfileRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class LeaderboardService {

    @Autowired
    private UserProfileRepo userProfileRepo;

    // 🏆 Returns all users sorted by eco score
    public List<UserProfile> getLeaderboard() {
        List<UserProfile> profiles = userProfileRepo.findAll();

        return profiles.stream()
                .sorted(Comparator.comparingDouble(this::calculateEcoScore).reversed())
                .collect(Collectors.toList());
    }

    // 👤 Get a user's rank and score
    public Map<String, Object> getUserRank(Long userId) {
        List<UserProfile> leaderboard = getLeaderboard();

        for (int i = 0; i < leaderboard.size(); i++) {
            UserProfile profile = leaderboard.get(i);
            if (profile.getUser().getId().equals(userId)) {
                Map<String, Object> result = new HashMap<>();
                result.put("rank", i + 1);
                result.put("ecoScore", calculateEcoScore(profile));
                result.put("carbonPoints", profile.getCarbonPoints());
                result.put("carbonSaved", profile.getCarbonSaved());
                result.put("user", profile.getUser().getUsername());
                return result;
            }
        }

        return Map.of("message", "User not found in leaderboard");
    }

    // 🔢 Weighted eco score formula
    private double calculateEcoScore(UserProfile profile) {
        return (profile.getCarbonPoints() * 0.7) + (profile.getCarbonSaved() * 0.3);
    }
}
