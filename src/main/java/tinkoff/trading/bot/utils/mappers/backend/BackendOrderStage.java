package tinkoff.trading.bot.utils.mappers.backend;

import lombok.Value;

@Value
public class BackendOrderStage {
    BackendMoneyValue price;
    long              quantity;
    String            tradeId;
}
