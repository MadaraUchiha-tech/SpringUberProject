package com.Navjeet.Uber.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RideRequest {

    @NotBlank(message = "pickupLocation must not be blank")
    @Size(min = 3, message = "pickupLocation must be at least 3 characters")
    private String pickupLocation;

    @NotBlank(message = "dropLocation must not be blank")
    @Size(min = 3, message = "dropLocation must be at least 3 characters")
    private String dropLocation;

    public RideRequest() {}

    public String getPickupLocation() { return pickupLocation; }
    public void setPickupLocation(String pickupLocation) { this.pickupLocation = pickupLocation; }

    public String getDropLocation() { return dropLocation; }
    public void setDropLocation(String dropLocation) { this.dropLocation = dropLocation; }
}