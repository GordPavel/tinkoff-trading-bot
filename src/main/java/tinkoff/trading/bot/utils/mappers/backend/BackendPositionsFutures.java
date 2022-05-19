package tinkoff.trading.bot.utils.mappers.backend;

import lombok.Value;

@Value
public class BackendPositionsFutures {
    String figi;
    long   blocked;
    long   balance;
}
