package com.yellowsunn.userservice.security;

import com.yellowsunn.userservice.security.encoder.PasswordEncoder;
import com.yellowsunn.userservice.security.encoder.SHA256PasswordEncoder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SHA256PasswordEncoderTest {

    PasswordEncoder passwordEncoder = new SHA256PasswordEncoder();

    @Test
    void encodeAndMatch() {
        //given
        String password1 = "q43f12cz3";
        String password2 = "a80rlkmd1!q";
        String password3 = "8!Tv4jL7ejv*at6W";
        String encodedPassword1 = passwordEncoder.encode(password1);
        String encodedPassword2 = passwordEncoder.encode(password2);
        String encodedPassword3 = passwordEncoder.encode(password3);

        //when
        boolean isSame1 = passwordEncoder.matches(password1, encodedPassword1);
        boolean isSame2 = passwordEncoder.matches(password2, encodedPassword2);
        boolean isSame3 = passwordEncoder.matches(password3, encodedPassword3);

        //then
        assertThat(password1).isNotEqualTo(encodedPassword1);
        assertThat(isSame1).isTrue();
        assertThat(password2).isNotEqualTo(encodedPassword2);
        assertThat(isSame2).isTrue();
        assertThat(password3).isNotEqualTo(encodedPassword3);
        assertThat(isSame3).isTrue();
        assertThat(encodedPassword1).isNotEqualTo(encodedPassword2);
        assertThat(encodedPassword2).isNotEqualTo(encodedPassword3);
    }

    @Test
    @DisplayName("비밀번호를 암호화할 때 공백이 포함되어 있으면 에러")
    void emptyPassword() {
        //given
        String password1 = "";
        String password2 = " ";
        String password3 = " a1235";
        String password4 = "a1 3235";

        //then
        assertThatThrownBy(() -> passwordEncoder.encode(password1)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> passwordEncoder.encode(password2)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> passwordEncoder.encode(password3)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> passwordEncoder.encode(password4)).isInstanceOf(IllegalArgumentException.class);
    }
}
