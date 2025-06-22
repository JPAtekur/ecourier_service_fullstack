package com.ecourier.trackingservice.service;

import com.ecourier.trackingservice.dto.CreateTrackingRequest;
import com.ecourier.trackingservice.dto.TrackingEntryDto;
import com.ecourier.trackingservice.entity.TrackingEntry;
import com.ecourier.trackingservice.repository.TrackingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TrackingService {
    private final TrackingRepository trackingRepository;

    public TrackingEntryDto createTracking(CreateTrackingRequest request, String userEmail) {
        TrackingEntry entry = new TrackingEntry();
        entry.setParcelId(request.getParcelId());
        entry.setStatus(request.getStatus());
        entry.setLocation(request.getLocation());
        entry.setDescription(request.getDescription());
        entry.setUpdatedBy(userEmail);
        entry.setCreatedAt(LocalDateTime.now());

        TrackingEntry saved = trackingRepository.save(entry);
        return mapToDto(saved);
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
                .orElseThrow(() -> new RuntimeException("No tracking found for parcel"));
    }

    private TrackingEntryDto mapToDto(TrackingEntry entry) {
        TrackingEntryDto dto = new TrackingEntryDto();
        BeanUtils.copyProperties(entry, dto);
        return dto;
    }
}
