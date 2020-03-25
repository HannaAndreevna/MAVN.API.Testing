package com.lykke.api.testing.api.common;

import static org.apache.commons.lang3.RandomStringUtils.random;
import static org.apache.commons.lang3.RandomStringUtils.randomNumeric;

import java.util.Random;
import java.util.UUID;
import lombok.experimental.UtilityClass;
import lombok.val;
import org.apache.commons.lang3.RandomStringUtils;

@UtilityClass
public class GenerateUtils {

    private static final String TEST_EMAIL_DOMAIN = "@example123.com";

    public static String generateName(int length) {
        return random(length, true, false);
    }

    public static String generateRandomString(int length) {
        return random(length, true, true);
    }

    public static String generateRandomString() {
        return generateRandomString(10);
    }

    public static String generateRandomPhone(int length) {
        return randomNumeric(length);
    }

    public static String generateRandomEmail() {
        // \\A(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?)\\Z
        // the customer profile service requires so
        return RandomStringUtils.randomAlphanumeric(63).toLowerCase() + TEST_EMAIL_DOMAIN;
    }

    public static String generateRandomHash() {
        // 0x[a-f0-9]{64}
        val chars = new char[]{'a', 'b', 'c', 'd', 'e', 'f', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        return "0x" + RandomStringUtils.random(64, chars);
    }

    public static String getRandomUuid() {
        return UUID.randomUUID().toString();
    }

    public static int generateRandomInt() {
        return new Random().nextInt(1000);
    }

    public static Double generateRandomDouble() {
        return new Random().nextDouble();
    }
}
