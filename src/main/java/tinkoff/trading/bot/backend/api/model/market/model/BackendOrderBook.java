package tinkoff.trading.bot.backend.api.model.market.model;


import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BackendOrderBook
        extends BackendOrderBookResponse
        implements BackendMarketDataDto {
    LocalDateTime time;
    boolean       isConsistent;
}
