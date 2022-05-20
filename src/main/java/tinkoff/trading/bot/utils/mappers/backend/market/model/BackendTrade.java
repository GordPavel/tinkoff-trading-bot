package tinkoff.trading.bot.utils.mappers.backend.market.model;

import lombok.Value;
import tinkoff.trading.bot.utils.mappers.backend.BackendQuotation;

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
