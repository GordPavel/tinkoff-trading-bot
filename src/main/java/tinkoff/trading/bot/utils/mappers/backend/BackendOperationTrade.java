package tinkoff.trading.bot.utils.mappers.backend;

import lombok.Value;

import java.time.LocalDateTime;

@Value
public class BackendOperationTrade {
    String            tradeId;
    LocalDateTime     dateTime;
    long              quantity;
    BackendMoneyValue price;
}
