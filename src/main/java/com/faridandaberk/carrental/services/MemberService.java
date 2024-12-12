package com.faridandaberk.carrental.services;

import com.faridandaberk.carrental.exception.ResourceNotFoundException;
import com.faridandaberk.carrental.model.Member;
import com.faridandaberk.carrental.model.Reservation;
import com.faridandaberk.carrental.model.ReservationStatus;
import com.faridandaberk.carrental.repository.MemberRepository;
import com.faridandaberk.carrental.struct.MemberStruct;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public MemberStruct registerMember(MemberStruct memberDTO) {

        // Validate input
        if (memberDTO == null) {
            throw new IllegalArgumentException("Member data cannot be null");
        }

        validateMemberData(memberDTO);

        // Check if email is already registered
        if (memberRepository.findByEmail(memberDTO.email()).isPresent()) {
            throw new IllegalStateException("Email is already registered");
        }

        // Check if driving license is already registered
        if (memberRepository.findByDrivingLicenseNumber(memberDTO.drivingLicenseNumber()).isPresent()) {
            throw new IllegalStateException("Driving license is already registered");
        }

        Member member = new Member();
        member.setName(memberDTO.name());
        member.setEmail(memberDTO.email());
        member.setPhone(memberDTO.phone());
        member.setAddress(memberDTO.address());
        member.setDrivingLicenseNumber(memberDTO.drivingLicenseNumber());
        member.setReservations(new HashSet<>());

        Member savedMember = memberRepository.save(member);
        return mapToDTO(savedMember);
    }

    public MemberStruct getMemberById(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found with id: " + memberId));
        return mapToDTO(member);
    }

    public List<MemberStruct> getAllMembers() {
        List<Member> members = memberRepository.findAll();
        if (members.isEmpty()) {
            throw new ResourceNotFoundException("No members found");
        }
        return members.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public MemberStruct updateMember(Long memberId, MemberStruct memberDTO) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found with id: " + memberId));

        if (memberDTO.email() != null && !memberDTO.email().equals(member.getEmail())) {
            memberRepository.findByEmail(memberDTO.email()).ifPresent(m -> {
                throw new IllegalStateException("Email is already in use");
            });
            member.setEmail(memberDTO.email());
        }

        if (memberDTO.name() != null) {
            member.setName(memberDTO.name());
        }
        if (memberDTO.phone() != null) {
            member.setPhone(memberDTO.phone());
        }
        if (memberDTO.address() != null) {
            member.setAddress(memberDTO.address());
        }
        if (memberDTO.drivingLicenseNumber() != null &&
                !memberDTO.drivingLicenseNumber().equals(member.getDrivingLicenseNumber())) {
            memberRepository.findByDrivingLicenseNumber(memberDTO.drivingLicenseNumber())
                    .ifPresent(m -> {
                        throw new IllegalStateException("Driving license is already registered");
                    });
            member.setDrivingLicenseNumber(memberDTO.drivingLicenseNumber());
        }

        Member updatedMember = memberRepository.save(member);
        return mapToDTO(updatedMember);
    }

    public boolean deleteMember(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found with id: " + memberId));

        // Check if member has active reservations
        if (hasActiveReservations(member)) {
            throw new IllegalStateException("Cannot delete member with active reservations");
        }

        memberRepository.delete(member);
        return true;
    }

    private boolean hasActiveReservations(Member member) {
        return member.getReservations().stream()
                .anyMatch(reservation ->
                        reservation.getStatus() == ReservationStatus.ACTIVE ||
                                reservation.getStatus() == ReservationStatus.PENDING ||
                                reservation.getStatus() == ReservationStatus.CONFIRMED
                );
    }

    private void validateMemberData(MemberStruct memberDTO) {
        if (memberDTO.name() == null || memberDTO.name().trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        if (memberDTO.email() == null || !isValidEmail(memberDTO.email())) {
            throw new IllegalArgumentException("Invalid email format");
        }
        if (memberDTO.phone() == null || !isValidPhone(memberDTO.phone())) {
            throw new IllegalArgumentException("Invalid phone number format");
        }
        if (memberDTO.drivingLicenseNumber() == null || memberDTO.drivingLicenseNumber().trim().isEmpty()) {
            throw new IllegalArgumentException("Driving license number cannot be empty");
        }
    }

    private boolean isValidEmail(String email) {
        // Basic email validation
        return email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }

    private boolean isValidPhone(String phone) {
        // Basic phone number validation
        return phone.matches("^\\+?[0-9]{10,15}$");
    }

    private MemberStruct mapToDTO(Member member) {
        return new MemberStruct(
                member.getName(),
                member.getAddress(),
                member.getEmail(),
                member.getPhone(),
                member.getDrivingLicenseNumber()
        );
    }

    public boolean resetPassword(Long memberId, String newPassword) {
        // Validate input
        if (newPassword == null || newPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("New password cannot be empty");
        }

        // Find member
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found with id: " + memberId));

        // Validate password requirements
        if (!isValidPassword(newPassword)) {
            throw new IllegalArgumentException("Password does not meet security requirements");
        }

        // Since there's no password field in your Member entity,
        // you might want to either:
        // 1. Add a password field to the Member entity
        // 2. Remove this functionality
        // For now, returning true as placeholder
        return true;
    }

    private boolean isValidPassword(String password) {
        // Password validation requirements:
        // At least 8 characters
        // Contains at least one digit
        // Contains at least one uppercase letter
        // Contains at least one lowercase letter
        return password.length() >= 8 &&
                password.matches(".*\\d.*") &&
                password.matches(".*[A-Z].*") &&
                password.matches(".*[a-z].*");
    }
}