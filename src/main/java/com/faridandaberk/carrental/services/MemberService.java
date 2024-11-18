package com.faridandaberk.carrental.services;

import com.faridandaberk.carrental.model.Member;
import com.faridandaberk.carrental.struct.MemberStruct;
import org.springframework.stereotype.Service;

@Service
public class MemberService {
  //  private final MemberRepository memberRepository;   ----ToDO waiting repo

    public Member registerMember(MemberStruct memberDTO) {
        return null;
    }

    public boolean deleteMember(Long memberId) {
        return false;
    }

    public boolean resetPassword(Long memberId, String newPassword) {
        return false;
    }
}