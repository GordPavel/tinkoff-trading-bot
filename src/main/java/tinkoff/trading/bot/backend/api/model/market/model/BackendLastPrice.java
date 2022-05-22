package tinkoff.trading.bot.backend.api.model.market.model;

import lombok.Value;
import tinkoff.trading.bot.backend.api.model.BackendQuotation;

import java.time.LocalDateTime;

@Value
public class BackendLastPrice
        implements BackendMarketDataDto {
    String           figi;
    BackendQuotation price;
    LocalDateTime    time;
}
