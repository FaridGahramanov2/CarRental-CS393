package com.faridandaberk.carrental.controller;

import com.faridandaberk.carrental.services.MemberService;
import com.faridandaberk.carrental.struct.MemberStruct;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/members")
@Tag(
        name = "Member Management",
        description = "APIs for managing rental system members including registration, deletion, and password management"
)
public class MemberController {
    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @Operation(
            summary = "Register a new member",
            description = "Register a new member in the rental system. Email and driving license must be unique."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Member registered successfully",
                    content = @Content(schema = @Schema(implementation = MemberStruct.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid member data provided"
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Email or driving license already registered"
            )
    })
    @PostMapping
    public ResponseEntity<MemberStruct> registerMember(
            @Parameter(
                    description = "Member details including name, email, phone, address, and driving license",
                    required = true
            )
            @RequestBody MemberStruct memberDTO) {
        return ResponseEntity.ok(memberService.registerMember(memberDTO));
    }

    @Operation(
            summary = "Delete a member",
            description = "Delete a member from the system. Cannot delete members with active reservations."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Member deleted successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Member not found"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Cannot delete member with active reservations"
            )
    })
    @DeleteMapping("/{memberId}")
    public ResponseEntity<Boolean> deleteMember(@PathVariable Long memberId) {
        try {
            return memberService.deleteMember(memberId) ? ResponseEntity.ok(true) : ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(
            summary = "Reset member's password",
            description = "Reset the password for a specific member"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Password reset successful"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Member not found"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid password format"
            )
    })
    @PutMapping("/{memberId}/password")
    public ResponseEntity<Boolean> resetPassword(
            @Parameter(description = "ID of the member", required = true)
            @PathVariable Long memberId,
            @Parameter(description = "New password (must meet security requirements)", required = true)
            @RequestBody String newPassword) {
        return memberService.resetPassword(memberId, newPassword) ?
                ResponseEntity.ok(true) :
                ResponseEntity.notFound().build();
    }
}