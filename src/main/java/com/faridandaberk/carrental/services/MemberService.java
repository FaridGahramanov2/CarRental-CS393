package com.faridandaberk.carrental.services;

import com.faridandaberk.carrental.model.Member;
import com.faridandaberk.carrental.repository.MemberRepository;
import com.faridandaberk.carrental.struct.MemberStruct;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Service
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public MemberStruct registerMember(MemberStruct memberDTO) {
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

    private MemberStruct mapToDTO(Member member) {
        return new MemberStruct(
                member.getName(),
                member.getAddress(),
                member.getEmail(),
                member.getPhone(),
                member.getDrivingLicenseNumber()
        );
    }

    public boolean deleteMember(Long memberId) {
        if (memberRepository.existsById(memberId)) {
            memberRepository.deleteById(memberId);
            return true;
        }
        return false;
    }

    public boolean resetPassword(Long memberId, String newPassword) {

        return true;
    }
}
