package com.yellowsunn.userservice.utils;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BCryptPasswordEncoderTest {

    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Test
    void shouldMatchesReturnTrue() {
        //given
        String password = "q43f12cz3";
        String encodedPassword = passwordEncoder.encode(password);

        //when
        boolean isSame = passwordEncoder.matches(password, encodedPassword);

        assertThat(isSame).isTrue();
    }

    @Test
    void shouldMatchesReturnFalse() {
        //given
        String encodedPassword = passwordEncoder.encode("q43f12cz3");
        String otherPassword = "12345678";

        // when
        boolean isSame = passwordEncoder.matches(otherPassword, encodedPassword);

        // then
        assertThat(isSame).isFalse();
    }
}
