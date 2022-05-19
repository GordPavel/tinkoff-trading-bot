package tinkoff.trading.bot.market.instrument;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;
import tinkoff.trading.bot.utils.mappers.backend.BackendMoneyValue;

@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Currency
        extends TradingInstrument {
    BackendMoneyValue nominal;
}
