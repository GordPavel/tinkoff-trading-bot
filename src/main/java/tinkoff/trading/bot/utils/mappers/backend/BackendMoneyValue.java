package tinkoff.trading.bot.utils.mappers.backend;

import lombok.Value;

@Value
public class BackendMoneyValue {
    String currency;
    long   units;
    int    nano;
}
