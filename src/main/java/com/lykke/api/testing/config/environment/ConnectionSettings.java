package com.lykke.api.testing.config.environment;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ConnectionSettings {

    private String url;
    private String username;
    private String password;
}
