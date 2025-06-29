package com.ecourier.trackingservice.repository;

import com.ecourier.trackingservice.entity.TrackingEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TrackingRepository extends JpaRepository<TrackingEntry, Long> {
    List<TrackingEntry> findByParcelIdOrderByCreatedAtDesc(Long parcelId);
    Optional<TrackingEntry> findFirstByParcelIdOrderByCreatedAtDesc(Long parcelId);
}
