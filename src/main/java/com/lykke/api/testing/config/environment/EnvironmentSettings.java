package com.lykke.api.testing.config.environment;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EnvironmentSettings {

    private ConnectionSettings backend;
    private ConnectionSettings frontend;
    private AdditionalSettings settings;
    private ImageSettings image;
    private EthereumSettings ethereum;
}
