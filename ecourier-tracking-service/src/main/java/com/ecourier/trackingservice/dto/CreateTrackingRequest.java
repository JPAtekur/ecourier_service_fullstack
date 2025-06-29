package com.ecourier.trackingservice.dto;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateTrackingRequest {
    @NotNull(message = "Parcel ID is required")
    private Long parcelId;

    @NotEmpty(message = "Status is required")
    @Pattern(regexp = "PENDING|PICKED_UP|IN_TRANSIT|DELIVERED|CANCELLED",
            message = "Invalid status value")
    private String status;

    @NotEmpty(message = "Location is required")
    private String location;

    @NotEmpty(message = "Description is required")
    @Size(min = 10, max = 500, message = "Description must be between 10 and 500 characters")
    private String description;
}
