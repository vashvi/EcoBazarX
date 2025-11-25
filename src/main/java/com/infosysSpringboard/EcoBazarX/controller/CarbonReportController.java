


package com.infosysSpringboard.EcoBazarX.controller;

import com.infosysSpringboard.EcoBazarX.model.CarbonInsight;
import com.infosysSpringboard.EcoBazarX.model.UserProfile;
import com.infosysSpringboard.EcoBazarX.model.Users;
import com.infosysSpringboard.EcoBazarX.repo.CarbonInsightRepository;
import com.infosysSpringboard.EcoBazarX.repo.UserProfileRepo;
import com.infosysSpringboard.EcoBazarX.repo.UserRepo;
import com.infosysSpringboard.EcoBazarX.service.CarbonReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/carbon")
public class CarbonReportController {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private UserProfileRepo userProfileRepo;

    @Autowired
    private CarbonInsightRepository carbonInsightRepository;

    @Autowired
    private CarbonReportService carbonReportService;

    @GetMapping("/user/total")
    public ResponseEntity<BigDecimal> getUserTotalCarbon(Authentication authentication) {
        String username = authentication.getName();
        return ResponseEntity.ok(carbonReportService.getUserTotalCarbon(username));
    }

    @GetMapping("/user/monthly")
    public ResponseEntity<Map<String, BigDecimal>> getMonthlyUserCarbon(Authentication authentication) {
        String username = authentication.getName();
        return ResponseEntity.ok(carbonReportService.getMonthlyUserCarbon(username));
    }

    @GetMapping("/admin/total")
    public ResponseEntity<BigDecimal> getPlatformTotalCarbon() {
        return ResponseEntity.ok(carbonReportService.getPlatformTotalCarbon());
    }

    @GetMapping("/user/eco-products")
    public ResponseEntity<?> getLowCarbonOrders(Authentication authentication) {
        String username = authentication.getName();
        return ResponseEntity.ok(carbonReportService.getRecentLowCarbonOrders(username));
    }
    @GetMapping("/user/points")
    public ResponseEntity<?> getUserCarbonPoints(Authentication auth) {

        Users user = userRepo.findByUsername(auth.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserProfile profile = userProfileRepo.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Profile missing"));

        List<CarbonInsight> history = carbonInsightRepository
                .findByUserOrderByRecordedAtDesc(user);

        return ResponseEntity.ok(Map.of(
                "carbonPoints", profile.getCarbonPoints(),
                "carbonSaved", profile.getCarbonSaved(),
                "badge", profile.getBadge(),
                "history", history.stream().map(ins -> Map.of(
                        "id", ins.getId(),
                        "totalCarbon", ins.getTotalCarbon(),
                        "orderCount", ins.getOrderCount(),
                        "recordedAt", ins.getRecordedAt()
                ))
        ));
    }

}