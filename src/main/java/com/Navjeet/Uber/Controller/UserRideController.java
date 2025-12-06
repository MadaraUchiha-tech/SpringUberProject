package com.Navjeet.Uber.Controller;

import com.Navjeet.Uber.model.Ride;
import com.Navjeet.Uber.service.RideService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user/rides")
public class UserRideController {

    private final RideService rideService;

    public UserRideController(RideService rideService) {
        this.rideService = rideService;
    }
    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<List<Ride>> getMyRides() {
        String username = org.springframework.security.core.context.SecurityContextHolder
                .getContext().getAuthentication().getName();

        List<Ride> rides = rideService.getRidesForUser(username);
        return ResponseEntity.ok(rides);
    }
}
