package com.faridandaberk.carrental.services;

import com.faridandaberk.carrental.exception.ResourceNotFoundException;
import com.faridandaberk.carrental.model.Member;
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

        // Check for existing email/license/phone
        if (memberRepository.existsByEmail(memberDTO.email())) {
            throw new IllegalStateException("Email is already registered");
        }
        if (memberRepository.existsByDrivingLicenseNumber(memberDTO.drivingLicenseNumber())) {
            throw new IllegalStateException("Driving license is already registered");
        }
        if (memberRepository.existsByPhone(memberDTO.phone())) {
            throw new IllegalStateException("Phone number is already registered");
        }

        Member member = new Member();
        member.setName(memberDTO.name());
        member.setEmail(memberDTO.email());
        member.setPhone(memberDTO.phone());
        member.setAddress(memberDTO.address());
        member.setDrivingLicenseNumber(memberDTO.drivingLicenseNumber());
        if (memberDTO.password() != null) {
            member.setPassword(memberDTO.password()); // In real app, should encrypt password
        }
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

        // Validate and update each field if provided
        if (memberDTO.email() != null && !memberDTO.email().equals(member.getEmail())) {
            if (!isValidEmail(memberDTO.email())) {
                throw new IllegalArgumentException("Invalid email format");
            }
            if (memberRepository.existsByEmail(memberDTO.email())) {
                throw new IllegalStateException("Email is already in use");
            }
            member.setEmail(memberDTO.email());
        }

        if (memberDTO.phone() != null && !memberDTO.phone().equals(member.getPhone())) {
            if (!isValidPhone(memberDTO.phone())) {
                throw new IllegalArgumentException("Invalid phone format");
            }
            if (memberRepository.existsByPhone(memberDTO.phone())) {
                throw new IllegalStateException("Phone is already in use");
            }
            member.setPhone(memberDTO.phone());
        }

        if (memberDTO.name() != null) {
            member.setName(memberDTO.name());
        }
        if (memberDTO.address() != null) {
            member.setAddress(memberDTO.address());
        }
        if (memberDTO.drivingLicenseNumber() != null &&
                !memberDTO.drivingLicenseNumber().equals(member.getDrivingLicenseNumber())) {
            if (memberRepository.existsByDrivingLicenseNumber(memberDTO.drivingLicenseNumber())) {
                throw new IllegalStateException("Driving license is already registered");
            }
            member.setDrivingLicenseNumber(memberDTO.drivingLicenseNumber());
        }

        Member updatedMember = memberRepository.save(member);
        return mapToDTO(updatedMember);
    }

    public boolean deleteMember(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found with id: " + memberId));

        if (hasActiveReservations(member)) {
            throw new IllegalStateException("Cannot delete member with active reservations");
        }

        memberRepository.delete(member);
        return true;
    }

    public boolean resetPassword(Long memberId, String newPassword) {
        if (newPassword == null || newPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("New password cannot be empty");
        }

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found with id: " + memberId));

        if (!isValidPassword(newPassword)) {
            throw new IllegalArgumentException("Password does not meet security requirements");
        }

        member.setPassword(newPassword); // In real app, should encrypt password
        memberRepository.save(member);
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
        if (!isValidEmail(memberDTO.email())) {
            throw new IllegalArgumentException("Invalid email format");
        }
        if (!isValidPhone(memberDTO.phone())) {
            throw new IllegalArgumentException("Invalid phone number format");
        }
        if (memberDTO.drivingLicenseNumber() == null || memberDTO.drivingLicenseNumber().trim().isEmpty()) {
            throw new IllegalArgumentException("Driving license number cannot be empty");
        }
    }

    private boolean isValidEmail(String email) {
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }

    private boolean isValidPhone(String phone) {
        return phone != null && phone.matches("\\d{10}");
    }

    private boolean isValidPassword(String password) {
        return password != null &&
                password.length() >= 8 &&
                password.matches(".*\\d.*") &&
                password.matches(".*[A-Z].*") &&
                password.matches(".*[a-z].*");
    }

    private MemberStruct mapToDTO(Member member) {
        return new MemberStruct(
                member.getName(),
                member.getEmail(),
                member.getPhone(),
                member.getAddress(),
                member.getDrivingLicenseNumber()
        );
    }



}