package com.Navjeet.Uber.Controller;

import com.Navjeet.Uber.model.Ride;
import com.Navjeet.Uber.service.RideService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/driver/rides")
public class DriverRideController {

    private final RideService rideService;

    public DriverRideController(RideService rideService) {
        this.rideService = rideService;
    }
    @GetMapping("/requests")
    @PreAuthorize("hasAuthority('ROLE_DRIVER')")
    public ResponseEntity<List<Ride>> getPendingRequests() {
        List<Ride> pending = rideService.getPendingRequests();
        return ResponseEntity.ok(pending);
    }
    @PostMapping("/{rideId}/accept")
    @PreAuthorize("hasAuthority('ROLE_DRIVER')")
    public ResponseEntity<Ride> acceptRide(@PathVariable String rideId) {
        String driverUsername = org.springframework.security.core.context.SecurityContextHolder
                .getContext().getAuthentication().getName();

        Ride accepted = rideService.acceptRide(driverUsername, rideId);
        return ResponseEntity.ok(accepted);
    }
}