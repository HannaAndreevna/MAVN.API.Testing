package com.lykke.api.testing.config;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TestSettings {

    private boolean jooqDebugMode;
    private boolean experimentalMode;
    private boolean dbcheckMode;
    private boolean inputValidationMode;
    private boolean restDebugMode;
    private String screenshotFolder;
    private int webDriverTimeout;
    private String chromeDriverPath;
}
