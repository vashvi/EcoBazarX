package com.infosysSpringboard.EcoBazarX.controller;

import com.infosysSpringboard.EcoBazarX.service.CarbonReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/api/carbon")
public class CarbonReportController {

    @Autowired
    private CarbonReportService carbonReportService;

    // 1️⃣ Total carbon footprint for logged-in user
    @GetMapping("/user/total")
    public ResponseEntity<BigDecimal> getUserTotalCarbon(@RequestAttribute("username") String username) {
        return ResponseEntity.ok(carbonReportService.getUserTotalCarbon(username));
    }

    // 2️⃣ Monthly trend for logged-in user
    @GetMapping("/user/monthly")
    public ResponseEntity<Map<String, BigDecimal>> getMonthlyUserCarbon(@RequestAttribute("username") String username) {
        return ResponseEntity.ok(carbonReportService.getMonthlyUserCarbon(username));
    }

    // 3️⃣ Platform total (admin)
    @GetMapping("/admin/total")
    public ResponseEntity<BigDecimal> getPlatformTotalCarbon() {
        return ResponseEntity.ok(carbonReportService.getPlatformTotalCarbon());
    }

    // 4️⃣ Suggest eco-friendly purchases
    @GetMapping("/user/eco-products")
    public ResponseEntity<?> getLowCarbonOrders(@RequestAttribute("username") String username) {
        return ResponseEntity.ok(carbonReportService.getRecentLowCarbonOrders(username));
    }
}
