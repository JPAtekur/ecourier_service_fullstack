package com.ecourier.trackingservice.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Table(name = "tracking_entries")
@Data
@NoArgsConstructor
public class TrackingEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long parcelId;
    private String status;
    private String location;
    private String description;
    private String updatedBy;

    @CreatedDate
    private LocalDateTime createdAt;
}