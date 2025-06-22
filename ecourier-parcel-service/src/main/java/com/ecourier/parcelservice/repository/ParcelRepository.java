package com.ecourier.parcelservice.repository;

import com.ecourier.parcelservice.entity.Parcel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParcelRepository extends JpaRepository<Parcel, Long> {
    List<Parcel> findBySenderId(String senderId);
    List<Parcel> findByAssignedAgentId(String assignedAgentId);
}
