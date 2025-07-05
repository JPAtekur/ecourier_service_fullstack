package com.ecourier.trackingservice.service;

import com.ecourier.trackingservice.config.FeignClientConfig;
import com.ecourier.trackingservice.dto.ParcelDto;
import com.ecourier.trackingservice.dto.UpdateStatusRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "ecourier-api-gateway", configuration = FeignClientConfig.class)
public interface ParcelClient {
    @GetMapping("/api/parcels/{id}")
    ParcelDto getParcel(@PathVariable Long id, @RequestHeader("Authorization") String token);

    @PutMapping("/api/parcels/update-status/{id}")
    ResponseEntity<String> updateParcelStatus(@PathVariable Long id,
                                      @RequestHeader("Authorization") String token,
                                      @RequestBody UpdateStatusRequest request);
}