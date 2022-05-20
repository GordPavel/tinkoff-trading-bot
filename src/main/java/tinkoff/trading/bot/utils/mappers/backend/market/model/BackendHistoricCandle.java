package tinkoff.trading.bot.utils.mappers.backend.market.model;

import lombok.Data;
import tinkoff.trading.bot.utils.mappers.backend.BackendQuotation;

import java.time.LocalDateTime;

@Data
public class BackendHistoricCandle
        implements BackendMarketDataDto {
    String           interval;
    BackendQuotation open;
    BackendQuotation high;
    BackendQuotation low;
    BackendQuotation close;
    int              volume;
    LocalDateTime    time;
}
