/*
package com.lykke.api.testing.ethereum;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.lykke.api.testing.api.common.EthereumUtils;
import org.junit.jupiter.api.Test;

public class EthereumTests {

    private static final String LINK_CODE = "52134";
    private static final String PRIVATE_KEY = "d69dac98cbaa219479cc617a0477e9a7431174d7244685a5aea041c5d49cca35";
    private static final String PRIVATE_ADDRESS = "0x80c08c4158c490bb504ff91d2b9b3ab914cb685f";
    private static final String PUBLIC_ADDRESS = "0x3ff662AF6C47e92d8F4BB23deeC3cf58AdBB2796";
    private static final String WEB3_JS_SIGNATURE = "0x69d345a649b7debb45eeae44c6642cd60571407df62898e1a3fdd6d969d8d4cb1e1724942acce6007537e51b5cd837b12a811f630e251f407597ca4b9ee81da31b";

    @Test
    void shouldSignLinkCode() {
        assertEquals(WEB3_JS_SIGNATURE, EthereumUtils.signLinkingCode(LINK_CODE, PRIVATE_KEY));
    }
}
*/