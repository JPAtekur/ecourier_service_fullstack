package com.ecourier.trackingservice.service;

import com.ecourier.trackingservice.dto.CreateTrackingRequest;
import com.ecourier.trackingservice.dto.ParcelDto;
import com.ecourier.trackingservice.dto.TrackingEntryDto;
import com.ecourier.trackingservice.dto.UpdateStatusRequest;
import com.ecourier.trackingservice.entity.TrackingEntry;
import com.ecourier.trackingservice.repository.TrackingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TrackingService {
    private final TrackingRepository trackingRepository;
    private final ParcelClient parcelClient;

    @Transactional
    public TrackingEntryDto createTracking(CreateTrackingRequest request, String userEmail) {
        // Verify parcel exists and user has access
        ParcelDto parcel = parcelClient.getParcel(
                request.getParcelId(),
                "Bearer " + SecurityContextHolder.getContext().getAuthentication().getCredentials().toString()
        );

        // Validate status transition
        validateStatusTransition(parcel.getStatus(), request.getStatus());

        TrackingEntry entry = new TrackingEntry();
        entry.setParcelId(request.getParcelId());
        entry.setStatus(request.getStatus());
        entry.setLocation(request.getLocation());
        entry.setDescription(request.getDescription());
        entry.setUpdatedBy(userEmail);
        entry.setCreatedAt(LocalDateTime.now());


        ResponseEntity<String> response = parcelClient.updateParcelStatus(
                request.getParcelId(),
                "Bearer " + SecurityContextHolder.getContext().getAuthentication().getCredentials().toString(),
                new UpdateStatusRequest(request.getStatus())
        );

        if (!response.getStatusCode().is2xxSuccessful()) {
            log.error("Failed to update parcel status: {}", response.getBody());
            throw new RuntimeException("Failed to update parcel status");
        }

        TrackingEntry saved = trackingRepository.save(entry);
        log.info("Created tracking entry for parcel {} with status {}",
                request.getParcelId(), request.getStatus());

        return mapToDto(saved);
    }

    private void validateStatusTransition(String currentStatus, String newStatus) {
        Map<String, List<String>> validTransitions = Map.of(
                "PENDING", List.of("PICKED_UP", "CANCELLED"),
                "PICKED_UP", List.of("IN_TRANSIT", "CANCELLED"),
                "IN_TRANSIT", List.of("DELIVERED", "CANCELLED")
        );

        if (!validTransitions.getOrDefault(currentStatus, List.of())
                .contains(newStatus)) {
            throw new IllegalStateException(
                    String.format("Invalid status transition from %s to %s",
                            currentStatus, newStatus)
            );
        }
    }

    public List<TrackingEntryDto> getTrackingHistory(Long parcelId) {
        return trackingRepository.findByParcelIdOrderByCreatedAtDesc(parcelId)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public TrackingEntryDto getLatestTracking(Long parcelId) {
        return trackingRepository.findFirstByParcelIdOrderByCreatedAtDesc(parcelId)
                .map(this::mapToDto)
                .orElseThrow(() -> new IllegalStateException("No tracking found for parcel: " + parcelId));
    }

    private TrackingEntryDto mapToDto(TrackingEntry entry) {
        TrackingEntryDto dto = new TrackingEntryDto();
        dto.setId(entry.getId());
        dto.setParcelId(entry.getParcelId());
        dto.setStatus(entry.getStatus());
        dto.setLocation(entry.getLocation());
        dto.setDescription(entry.getDescription());
        dto.setUpdatedBy(entry.getUpdatedBy());
        dto.setCreatedAt(entry.getCreatedAt());
        return dto;
    }
}
