package tinkoff.trading.bot.backend.api.model.market.model;

import lombok.Value;
import tinkoff.trading.bot.backend.api.model.BackendQuotation;

import java.time.LocalDateTime;

@Value
public class BackendTrade
        implements BackendMarketDataDto {
    String           figi;
    String           direction;
    BackendQuotation price;
    long             quantity;
    LocalDateTime    time;
}
