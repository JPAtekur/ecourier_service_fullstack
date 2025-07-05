package com.ecourier.trackingservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UpdateStatusRequest {
    private String status;
}
