package tinkoff.trading.bot.backend.api.model;

import lombok.Value;

import java.time.LocalDateTime;

@Value
public class BackendStopOrder {
    String            stopOrderId;
    String            figi;
    String            currency;
    String            direction;
    String            orderType;
    LocalDateTime     createDate;
    LocalDateTime     activationDateTime;
    LocalDateTime     expirationTime;
    long              lotsRequested;
    BackendMoneyValue price;
    BackendMoneyValue stopPrice;
}
