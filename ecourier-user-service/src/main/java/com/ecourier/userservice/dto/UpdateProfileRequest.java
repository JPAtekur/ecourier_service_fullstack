package com.ecourier.userservice.dto;

import lombok.Data;

@Data
public class UpdateProfileRequest {
    private String username;
    private String phone;
}