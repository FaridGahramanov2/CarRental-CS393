package com.faridandaberk.carrental.repository;

import com.faridandaberk.carrental.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);
    Optional<Member> findByDrivingLicenseNumber(String licenseNumber);
    Optional<Member> findByPhone(String phone);
    boolean existsByEmail(String email);
    boolean existsByDrivingLicenseNumber(String licenseNumber);
    boolean existsByPhone(String phone);
}