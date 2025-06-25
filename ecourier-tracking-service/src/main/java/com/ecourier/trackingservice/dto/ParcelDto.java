package com.ecourier.trackingservice.dto;

import lombok.Data;

@Data
public class ParcelDto {
    private Long id;
    private String status;
    private String fromAddress;
    private String toAddress;
    private String assignedAgentId;
    private String customerEmail;
}