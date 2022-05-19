package tinkoff.trading.bot.market.instrument;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;
import tinkoff.trading.bot.utils.mappers.backend.BackendQuotation;

import java.time.LocalDateTime;

@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Etf
        extends TradingInstrument {
    LocalDateTime    releasedDate;
    BackendQuotation fixedCommission;
    String           sector;
    String           focusType;
    String           rebalancingFreq;
    BackendQuotation numShares;
}
