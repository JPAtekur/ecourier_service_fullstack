package com.ecourier.trackingservice.controller;

import com.ecourier.trackingservice.dto.CreateTrackingRequest;
import com.ecourier.trackingservice.dto.TestDto;
import com.ecourier.trackingservice.dto.TrackingEntryDto;
import com.ecourier.trackingservice.service.TrackingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tracking")
@RequiredArgsConstructor
public class TrackingController {
    private final TrackingService trackingService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'AGENT')")
    public ResponseEntity<TrackingEntryDto> createTracking(
            @Valid @RequestBody CreateTrackingRequest request,
            Authentication auth) {
        return ResponseEntity.ok(trackingService.createTracking(request, auth.getName()));
    }

    @GetMapping("/{parcelId}")
    public ResponseEntity<List<TrackingEntryDto>> getTrackingHistory(@PathVariable Long parcelId) {
        return ResponseEntity.ok(trackingService.getTrackingHistory(parcelId));
    }

    @GetMapping("/{parcelId}/latest")
    public ResponseEntity<TrackingEntryDto> getLatestTracking(@PathVariable Long parcelId) {
        return ResponseEntity.ok(trackingService.getLatestTracking(parcelId));
    }
}
