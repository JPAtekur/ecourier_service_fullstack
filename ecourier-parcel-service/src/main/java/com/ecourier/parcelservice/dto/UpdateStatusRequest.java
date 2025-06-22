package com.ecourier.parcelservice.dto;

import com.ecourier.parcelservice.enums.ParcelStatus;
import lombok.Data;

@Data
public class UpdateStatusRequest {
    private String status;
}
