package com.ecourier.parcelservice.dto;

import lombok.Data;

@Data
public class BookParcelRequest {
    private String receiverName;
    private String deliveryAddress;
}
