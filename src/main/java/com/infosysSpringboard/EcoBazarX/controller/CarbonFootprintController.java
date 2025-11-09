package com.infosysSpringboard.EcoBazarX.controller;

import com.infosysSpringboard.EcoBazarX.dto.CarbonFootprintRequest;
import com.infosysSpringboard.EcoBazarX.model.CarbonEstimate;
import com.infosysSpringboard.EcoBazarX.model.Users;
import com.infosysSpringboard.EcoBazarX.repo.CarbonInsightRepository;
import com.infosysSpringboard.EcoBazarX.repo.UserRepo;
import com.infosysSpringboard.EcoBazarX.service.CarbonFootprintService;
import com.infosysSpringboard.EcoBazarX.service.JWTService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/carbon")
@CrossOrigin("*")
public class CarbonFootprintController {

    @Autowired
    private CarbonFootprintService service;

    @Autowired
    private JWTService jwtUtil;

    @Autowired
    private UserRepo userRepository;

    @Autowired
    private CarbonInsightRepository carbonInsightRepository;

//    @PostMapping("/estimate")
//    public ResponseEntity<?> estimate(@RequestBody Map<String, String> request) {
//        String description = request.get("description");
//        if (description == null || description.isEmpty()) {
//            return ResponseEntity.badRequest().body(Map.of("error", "Product description is required"));
//        }
//
//        CarbonEstimate estimate = service.getCarbonEstimate(description);
//        return ResponseEntity.ok( estimate);
//    }

    @PostMapping("/estimate")
    public ResponseEntity<?> estimate(@RequestBody CarbonFootprintRequest request) {
        if (request.getCarbonDetails() == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Carbon details are required"));
        }

        CarbonEstimate estimate = service.getCarbonEstimateFromDetails(request.getCarbonDetails());
        return ResponseEntity.ok(estimate);
    }



    @GetMapping("/user/history")
    public ResponseEntity<?> getUserCarbonHistory(HttpServletRequest request) {
        try {
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(401).body(Map.of("error", "Missing or invalid Authorization header"));
            }

            String token = authHeader.substring(7);
            String username = jwtUtil.extractUsername(token);

            Users user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            var insights = carbonInsightRepository.findByUserOrderByRecordedAtDesc(user);

            return ResponseEntity.ok(Map.of(
                    "username", username,
                    "totalRecords", insights.size(),
                    "insights", insights
            ));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }
}
