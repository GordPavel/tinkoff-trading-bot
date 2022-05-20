package tinkoff.trading.bot.utils.mappers.backend.market.model;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BackendCandle
        extends BackendHistoricCandle {
    String        figi;
    LocalDateTime lastTradeTs;
}
