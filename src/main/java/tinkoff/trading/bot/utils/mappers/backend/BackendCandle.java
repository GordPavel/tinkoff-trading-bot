package tinkoff.trading.bot.utils.mappers.backend;

import lombok.Value;

import java.time.LocalDateTime;

@Value
public class BackendCandle {
    String           figi;
    String           interval;
    BackendQuotation open;
    BackendQuotation high;
    BackendQuotation low;
    BackendQuotation close;
    int              volume;
    LocalDateTime    time;
    LocalDateTime    lastTradeTs;
}
