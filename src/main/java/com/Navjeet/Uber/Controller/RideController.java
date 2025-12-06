package com.Navjeet.Uber.Controller;

import com.Navjeet.Uber.DTO.RideRequest;
import com.Navjeet.Uber.model.Ride;
import com.Navjeet.Uber.service.RideService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.net.URI;

@RestController
@RequestMapping("/api/v1/rides")
public class RideController {

    private final RideService rideService;

    public RideController(RideService rideService) {
        this.rideService = rideService;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<?> requestRide(@Valid @RequestBody RideRequest rideRequest) {

        String username = org.springframework.security.core.context.SecurityContextHolder
                .getContext().getAuthentication().getName();

        Ride created = rideService.requestRide(username, rideRequest);

        return ResponseEntity.created(URI.create("/api/v1/rides/" + created.getId())).body(created);
    }

    @PostMapping("/{rideId}/complete")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Ride> completeRide(@PathVariable String rideId) {
        String callerUsername = org.springframework.security.core.context.SecurityContextHolder
                .getContext().getAuthentication().getName();

        Ride completed = rideService.completeRide(callerUsername, rideId);
        return ResponseEntity.ok(completed);
    }
}