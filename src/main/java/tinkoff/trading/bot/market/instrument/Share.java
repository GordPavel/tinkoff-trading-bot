package tinkoff.trading.bot.market.instrument;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;
import tinkoff.trading.bot.backend.api.model.BackendMoneyValue;

import java.time.LocalDateTime;

@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Share
        extends TradingInstrument {
    String            shareType;
    BackendMoneyValue nominal;
    String            sector;
    long              issueSizePlan;
    long              issueSize;
    boolean           divYieldFlag;
    LocalDateTime     ipoDate;
}
