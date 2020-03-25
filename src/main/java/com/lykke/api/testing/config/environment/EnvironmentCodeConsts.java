package com.lykke.api.testing.config.environment;

import lombok.Data;
import lombok.experimental.FieldNameConstants;

@FieldNameConstants
@Data
public class EnvironmentCodeConsts {
    private final String local;
    private final String ci;
}
