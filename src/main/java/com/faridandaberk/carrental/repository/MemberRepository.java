package com.faridandaberk.carrental.repository;

import com.faridandaberk.carrental.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);
    Optional<Member> findByDrivingLicenseNumber(String licenseNumber);
}