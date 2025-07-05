package com.ecourier.parcelservice.service;

import com.ecourier.parcelservice.dto.BookParcelRequest;
import com.ecourier.parcelservice.dto.UpdateStatusRequest;
import com.ecourier.parcelservice.dto.UserDto;
import com.ecourier.parcelservice.entity.Parcel;
import com.ecourier.parcelservice.enums.ParcelStatus;
import com.ecourier.parcelservice.repository.ParcelRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ParcelService {

    private final ParcelRepository parcelRepository;
    private final UserClient userClient;

    public void bookParcel(String senderId, BookParcelRequest request) {
        Parcel parcel = Parcel.builder()
                .senderId(senderId)
                .receiverName(request.getReceiverName())
                .deliveryAddress(request.getDeliveryAddress())
                .status(ParcelStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();

        parcelRepository.save(parcel);
    }

    @Transactional
    public void updateStatus(Long id, String userEmail, UpdateStatusRequest request) {
        Parcel parcel = parcelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Parcel not found"));

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String role = auth.getAuthorities().stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .orElse("");

        // Admin can update any parcel
        // Agent can only update assigned parcels
        if (role.equals("ROLE_AGENT") && !userEmail.equals(parcel.getAssignedAgentId())) {
            log.error("Agent {} not authorized to update parcel {}", userEmail, id);
            throw new RuntimeException("Unauthorized to update this parcel");
        }

        parcel.setStatus(ParcelStatus.valueOf(request.getStatus().toUpperCase()));
        parcelRepository.save(parcel);
        log.info("Updated parcel {} status to {}", id, request.getStatus());
    }

    public List<Parcel> getParcelsForUser(String userId, String role) {
        if ("AGENT".equalsIgnoreCase(role)) {
            return parcelRepository.findByAssignedAgentId(userId);
        } else {
            return parcelRepository.findBySenderId(userId);
        }
    }

    public void assignAgentToParcel(Long parcelId, String agentEmail) {
        Parcel parcel = parcelRepository.findById(parcelId)
                .orElseThrow(() -> new RuntimeException("Parcel not found"));

        String token = SecurityContextHolder.getContext().getAuthentication().getCredentials().toString();
        UserDto agent = userClient.getUserInfo(agentEmail, "Bearer " + token);

        if (!"AGENT".equalsIgnoreCase(agent.getRole())) {
            throw new RuntimeException("Provided user is not an agent");
        }

        parcel.setAssignedAgentId(agentEmail);
        parcelRepository.save(parcel);
    }

    @Transactional(readOnly = true)
    public Parcel getParcelById(Long id) {
        return parcelRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Parcel not found with id: " + id));
    }

}
