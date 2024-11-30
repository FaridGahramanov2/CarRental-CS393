package com.faridandaberk.carrental;

import com.faridandaberk.carrental.services.MemberService;
import com.faridandaberk.carrental.struct.MemberStruct;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @Test
    void registerMember_RegistersMember_ReturnsMemberDTO() {

        MemberStruct memberDTO = new MemberStruct(
                "John Doe",
                "123 Main St",
                "test.user@example.com",
                "555-1234",
                "TEST_DL_123"
        );


        MemberStruct result = memberService.registerMember(memberDTO);


        assertThat(result).isNotNull();
        assertThat(result.name()).isEqualTo("John Doe");
        assertThat(result.email()).isEqualTo("test.user@example.com");
        assertThat(result.drivingLicenseNumber()).isEqualTo("TEST_DL_123");
    }
}