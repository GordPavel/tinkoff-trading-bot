package tinkoff.trading.bot.backend.api.model;

import lombok.Value;

@Value
public class BackendPositionsFutures {
    String figi;
    long   blocked;
    long   balance;
}
