package com.ecourier.trackingservice.service;

import com.ecourier.trackingservice.config.FeignClientConfig;
import com.ecourier.trackingservice.dto.ParcelDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "parcel-service", url = "${parcel.service.url}", configuration = FeignClientConfig.class)
public interface ParcelClient {
    @GetMapping("/api/parcels/{id}")
    ParcelDto getParcel(@PathVariable Long id, @RequestHeader("Authorization") String token);
}