package com.ecourier.parcelservice.controller;

import com.ecourier.parcelservice.dto.AssignAgentRequest;
import com.ecourier.parcelservice.dto.BookParcelRequest;
import com.ecourier.parcelservice.dto.UpdateStatusRequest;
import com.ecourier.parcelservice.dto.UserDto;
import com.ecourier.parcelservice.entity.Parcel;
import com.ecourier.parcelservice.service.ParcelService;
import com.ecourier.parcelservice.service.UserClient;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/parcels")
@RequiredArgsConstructor
public class ParcelController {

    private final ParcelService parcelService;
    private final UserClient userClient;

    @PostMapping("/book")
    public String bookParcel(@AuthenticationPrincipal UserDetails userDetails,
                             @RequestBody BookParcelRequest request) {
        parcelService.bookParcel(userDetails.getUsername(), request);
        return "Parcel booked successfully";
    }

    @PutMapping("/update-status/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('AGENT')")
    public String updateStatus(@AuthenticationPrincipal UserDetails userDetails,
                               @PathVariable Long id,
                               @RequestBody UpdateStatusRequest request) {
        parcelService.updateStatus(id, userDetails.getUsername(), request);
        return "Parcel status updated";
    }

    @GetMapping("/my-parcels")
    public List<Parcel> myParcels(@AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();
        String token = SecurityContextHolder.getContext().getAuthentication().getCredentials().toString();
        UserDto userDto = userClient.getUserInfo(email, "Bearer " + token);
        return parcelService.getParcelsForUser(email, userDto.getRole());
    }

    @PutMapping("/assign")
    @PreAuthorize("hasRole('ADMIN')")
    public String assignAgent(@RequestBody AssignAgentRequest request) {
        parcelService.assignAgentToParcel(request.getParcelId(), request.getAgentEmail());
        return "Agent assigned successfully.";
    }

    @GetMapping("/{id}")
    public Parcel getParcelById(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();
        String token = SecurityContextHolder.getContext().getAuthentication().getCredentials().toString();
        UserDto userDto = userClient.getUserInfo(email, "Bearer " + token);

        Parcel parcel = parcelService.getParcelById(id);

        // Only allow access if user is ADMIN, AGENT, or the parcel owner
        if (userDto.getRole().equals("ADMIN") ||
                userDto.getRole().equals("AGENT") ||
                parcel.getSenderId().equals(email)) {
            return parcel;
        }

        throw new AccessDeniedException("You don't have permission to view this parcel");
    }

}
