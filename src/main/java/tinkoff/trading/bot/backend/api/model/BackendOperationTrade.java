package tinkoff.trading.bot.backend.api.model;

import lombok.Value;

import java.time.LocalDateTime;

@Value
public class BackendOperationTrade {
    String            tradeId;
    LocalDateTime     dateTime;
    long              quantity;
    BackendMoneyValue price;
}
