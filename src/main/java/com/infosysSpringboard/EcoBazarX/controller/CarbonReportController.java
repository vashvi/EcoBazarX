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

    @GetMapping("/user/total")
    public ResponseEntity<BigDecimal> getUserTotalCarbon(@RequestAttribute("username") String username) {
        return ResponseEntity.ok(carbonReportService.getUserTotalCarbon(username));
    }

    @GetMapping("/user/monthly")
    public ResponseEntity<Map<String, BigDecimal>> getMonthlyUserCarbon(@RequestAttribute("username") String username) {
        return ResponseEntity.ok(carbonReportService.getMonthlyUserCarbon(username));
    }

    @GetMapping("/admin/total")
    public ResponseEntity<BigDecimal> getPlatformTotalCarbon() {
        return ResponseEntity.ok(carbonReportService.getPlatformTotalCarbon());
    }

    @GetMapping("/user/eco-products")
    public ResponseEntity<?> getLowCarbonOrders(@RequestAttribute("username") String username) {
        return ResponseEntity.ok(carbonReportService.getRecentLowCarbonOrders(username));
    }
}
