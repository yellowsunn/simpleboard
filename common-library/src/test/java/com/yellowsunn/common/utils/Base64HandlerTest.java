package com.yellowsunn.common.utils;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class Base64HandlerTest {
    @Test
    void encode() {
        // given
        var example = "example";

        // when
        String encoded = Base64Handler.encode(example);

        // then
        assertThat(encoded).isEqualTo("ZXhhbXBsZQ==");
    }

    @Test
    void decode() {
        // given
        var encodedExample = "ZXhhbXBsZQ==";

        // when
        String decoded = Base64Handler.decode(encodedExample);

        // then
        assertThat(decoded).isEqualTo("example");
    }
}
