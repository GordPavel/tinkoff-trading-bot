package tinkoff.trading.bot.utils.mappers.backend.market.model;

import lombok.Value;
import tinkoff.trading.bot.utils.mappers.backend.BackendQuotation;

import java.time.LocalDateTime;

@Value
public class BackendLastPrice
        implements BackendMarketDataDto {
    String           figi;
    BackendQuotation price;
    LocalDateTime    time;
}
