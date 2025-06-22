package com.ecourier.parcelservice.dto;

import lombok.Data;

@Data
public class AssignAgentRequest {
    private Long parcelId;
    private String agentEmail;
}

