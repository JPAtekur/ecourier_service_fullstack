package com.ecourier.trackingservice.dto;


import lombok.Data;

@Data
public class CreateTrackingRequest {
    private Long parcelId;
    private String status;
    private String location;
    private String description;
}
