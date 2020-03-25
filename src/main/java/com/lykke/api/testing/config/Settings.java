package com.lykke.api.testing.config;

import com.lykke.api.testing.config.environment.EnvironmentSettings;
import com.lykke.api.testing.config.environment.EnvironmentSelector;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Settings {

    private EnvironmentSelector environment;
    private TestSettings testSettings;
    private EnvironmentSettings environmentSettings;
}
