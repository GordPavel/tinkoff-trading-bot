package tinkoff.trading.bot.backend.api.model.market.model;

import lombok.Data;
import tinkoff.trading.bot.backend.api.model.BackendQuotation;

import java.time.LocalDateTime;

@Data
public class BackendHistoricCandle
        implements BackendMarketDataDto {
    BackendQuotation open;
    BackendQuotation high;
    BackendQuotation low;
    BackendQuotation close;
    int              volume;
    LocalDateTime    time;
}
