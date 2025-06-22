package com.ecourier.parcelservice.entity;

import com.ecourier.parcelservice.enums.ParcelStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Parcel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String senderId; // user email or UUID from JWT
    private String receiverName;
    private String deliveryAddress;

    @Enumerated(EnumType.STRING)
    private ParcelStatus status;

    private String assignedAgentId; // agent email or ID

    private LocalDateTime createdAt;
}