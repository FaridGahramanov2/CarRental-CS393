package com.faridandaberk.carrental.controller;

import com.faridandaberk.carrental.services.MemberService;
import com.faridandaberk.carrental.struct.MemberStruct;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/members")
public class MemberController {
    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping
    public ResponseEntity<MemberStruct> registerMember(@RequestBody MemberStruct memberDTO) {
        return ResponseEntity.ok(memberService.registerMember(memberDTO));
    }

    @DeleteMapping("/{memberId}")
    public ResponseEntity<Boolean> deleteMember(@PathVariable Long memberId) {
        return memberService.deleteMember(memberId) ?
                ResponseEntity.ok(true) :
                ResponseEntity.notFound().build();
    }

    @PutMapping("/{memberId}/password")
    public ResponseEntity<Boolean> resetPassword(
            @PathVariable Long memberId,
            @RequestBody String newPassword) {
        return memberService.resetPassword(memberId, newPassword) ?
                ResponseEntity.ok(true) :
                ResponseEntity.notFound().build();
    }
}