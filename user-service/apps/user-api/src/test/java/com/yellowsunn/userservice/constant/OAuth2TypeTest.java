package com.yellowsunn.userservice.constant;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

class OAuth2TypeTest {

    @Test
    void convert_String_to_OAuth2Type() {
        // given
        var type = "google";

        // when
        var oAuth2Type = OAuth2Type.convertFrom(type);

        // then
        assertThat(oAuth2Type).isEqualTo(OAuth2Type.GOOGLE);
    }

    @Test
    void convert_failed_when_unknown_type() {
        // given
        var type = "Unknown";

        // when
        Throwable throwable = catchThrowable(() -> OAuth2Type.convertFrom(type));

        // then
        assertThat(throwable).isInstanceOf(IllegalArgumentException.class);
    }
}
