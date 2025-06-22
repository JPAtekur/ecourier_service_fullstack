package com.ecourier.parcelservice.service;

import com.ecourier.parcelservice.dto.UserDto;
import com.ecourier.parcelservice.security.FeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "user-service", url = "${user.service.url}", configuration = FeignClientConfig.class)
public interface UserClient {
    @GetMapping("/api/auth/info/{email}")
    UserDto getUserInfo(@PathVariable String email, @RequestHeader("Authorization") String token);
}
