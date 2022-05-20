package tinkoff.trading.bot.utils.mappers.backend.market.model;


import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

@Value
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BackendOrderBook
        extends BackendOrderBookResponse
        implements BackendMarketDataDto {
    boolean isConsistent;
}
