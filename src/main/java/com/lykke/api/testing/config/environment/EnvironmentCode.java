package com.lykke.api.testing.config.environment;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum EnvironmentCode {
    DEV("dev"),
    TEST("test");

    @Getter
    private String code;
}
