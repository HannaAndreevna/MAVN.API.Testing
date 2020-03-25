package com.lykke.api.testing.api.common;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.RandomStringUtils.randomNumeric;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.var;

@AllArgsConstructor
@Builder
public class PasswordGen {

    public static final int MIN_LENGTH = 8;
    public static final int MAX_LENGTH = 50;
    public static final int MIN_UPPER_CASE = 1;
    public static final int MIN_LOWER_CASE = 1;
    public static final int MIN_SPECIAL_SYMBOLS = 1;
    public static final int MIN_NUMBERS = 1;
    public static final String ALLOWED_SPECIAL_SYMBOLS = "!@#$%&";
    public static final boolean ALLOW_WHITE_SPACES = false;

    private int minLength;
    private int maxLength;
    private boolean useLower;
    private boolean useUpper;
    private boolean useDigits;
    private boolean usePunctuation;

    public static String generateValidPassword() {
        return PasswordGen.builder()
                .minLength(MIN_LENGTH)
                .maxLength(MAX_LENGTH)
                .useUpper(true)
                .useLower(true)
                .useDigits(true)
                .usePunctuation(true)
                .build()
                .generatePassword(false);
    }

    public static String generateInvalidPassword(int minLength, int maxLength, boolean useUpper, boolean useLower,
            boolean useDigits, boolean usePunctuation) {
        return PasswordGen.builder()
                .minLength(minLength)
                .maxLength(maxLength)
                .useUpper(useUpper)
                .useLower(useLower)
                .useDigits(useDigits)
                .usePunctuation(usePunctuation)
                .build()
                .generatePassword(true);
    }

    private String generatePassword(boolean isInvalid) {
        int length = new Random().nextInt((maxLength - minLength) + 1) + minLength;

        StringBuilder password = new StringBuilder(length);
        Random random = new Random(System.nanoTime());

        int placeSetter = 0;
        // Collect the categories to use.
        List<String> charCategories = new ArrayList<>();
        if (useLower) {
            charCategories.add(randomAlphabetic(1).toLowerCase());
            placeSetter++;
        }
        if (useUpper) {
            charCategories.add(randomAlphabetic(1).toUpperCase());
            placeSetter++;
        }
        if (useDigits) {
            charCategories.add(randomNumeric(1));
            placeSetter++;
        }
        if (usePunctuation) {
            charCategories.add(String
                    .valueOf(ALLOWED_SPECIAL_SYMBOLS.toCharArray()[random.nextInt(ALLOWED_SPECIAL_SYMBOLS.length())]));
            placeSetter++;
        }

        // Build the password.
        for (int i = 0, j = 0; i < length; i++, j = i < charCategories.size() ? i : i % charCategories.size()) {
            String charCategory = charCategories.get(j);
            password.append(charCategory);
        }

        return new String(password);
    }
}