package tinkoff.trading.bot.backend.api.model;

import lombok.Value;

@Value
public class BackendOrderStage {
    BackendMoneyValue price;
    long              quantity;
    String            tradeId;
}
