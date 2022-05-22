package tinkoff.trading.bot.backend.api.model;

import lombok.Value;

import java.math.BigDecimal;

@Value
public class BackendMoneyValue {
    String     currency;
    BigDecimal value;
}
