package com.lykke.api.testing.config.environment;

import lombok.Data;

@Data
public class EnvironmentSelector {
    private EnvironmentCode code;

    public void setCode(String codeParam) {
        code = Enum.valueOf(EnvironmentCode.class, codeParam.toUpperCase());
    }

    public String getCode() {
        return code.getCode().toLowerCase();
    }
}
