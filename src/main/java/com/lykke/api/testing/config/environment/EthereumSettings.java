package com.lykke.api.testing.config.environment;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EthereumSettings {

    private String walletPublicAddress;
    private String walletPrivateKey;
    private String transitAccount;
    private String tokenIssueAddress;
    private String mvnVoucherGatewayAddress;
}
