package com.ecourier.trackingservice.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TrackingEntryDto {
    private Long id;
    private Long parcelId;
    private String status;
    private String location;
    private String description;
    private String updatedBy;
    private LocalDateTime createdAt;
}
