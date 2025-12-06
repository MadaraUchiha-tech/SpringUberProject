package com.Navjeet.Uber.service;

import com.Navjeet.Uber.DTO.RideRequest;
import com.Navjeet.Uber.exception.*;
import com.Navjeet.Uber.model.Ride;
import com.Navjeet.Uber.model.User;
import com.Navjeet.Uber.repository.RideRepository;
import com.Navjeet.Uber.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class RideService {

    private final RideRepository rideRepository;
    private final UserRepository userRepository;

    public RideService(RideRepository rideRepository, UserRepository userRepository) {
        this.rideRepository = rideRepository;
        this.userRepository = userRepository;
    }
    public Ride requestRide(String username, RideRequest req) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("user not found"));

        if (user.getRole() == null || !user.getRole().equals("ROLE_USER")) {
            throw new IllegalStateException("only users with ROLE_USER can request rides");
        }

        Ride ride = new Ride();
        ride.setUserId(user.getId());
        ride.setPickupLocation(req.getPickupLocation());
        ride.setDropLocation(req.getDropLocation());
        ride.setStatus("REQUESTED");

        return rideRepository.save(ride);
    }
    public List<Ride> getPendingRequests() {
        return rideRepository.findByStatus("REQUESTED");
    }
    @Transactional
    public Ride acceptRide(String driverUsername, String rideId) {
        User driver = userRepository.findByUsername(driverUsername)
                .orElseThrow(() -> new NotFoundException("driver not found"));

        if (driver.getRole() == null || !driver.getRole().equals("ROLE_DRIVER")) {
            throw new IllegalStateException("only users with ROLE_DRIVER can accept rides");
        }

        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() -> new NotFoundException("ride not found"));

        if (!"REQUESTED".equals(ride.getStatus())) {
            throw new BadRequestException("ride is not in REQUESTED state");
        }

        ride.setDriverId(driver.getId());
        ride.setStatus("ACCEPTED");

        return rideRepository.save(ride);
    }
    @Transactional
    public Ride completeRide(String callerUsername, String rideId) {
        User caller = userRepository.findByUsername(callerUsername)
                .orElseThrow(() -> new NotFoundException("user not found"));

        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() -> new NotFoundException("ride not found"));

        if (!"ACCEPTED".equals(ride.getStatus())) {
            throw new BadRequestException("ride must be in ACCEPTED status to be completed");
        }

        boolean isPassenger = caller.getId() != null && caller.getId().equals(ride.getUserId());
        boolean isDriver = caller.getId() != null && caller.getId().equals(ride.getDriverId());

        if (!isPassenger && !isDriver) {
            throw new IllegalStateException("only the requesting user or assigned driver can complete the ride");
        }

        ride.setStatus("COMPLETED");
        return rideRepository.save(ride);
    }
    public List<Ride> getRidesForUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("user not found"));

        return rideRepository.findByUserId(user.getId());
    }
}