package tinkoff.trading.bot.market.instrument;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;
import tinkoff.trading.bot.backend.api.model.BackendMoneyValue;

import java.time.LocalDateTime;

@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Bond
        extends TradingInstrument {
    BackendMoneyValue nominal;
    LocalDateTime     maturityDate;
    BackendMoneyValue placementPrice;
    String            sector;
    int               couponQuantityPerYear;
    LocalDateTime     stateRegDate;
    long              issueSizePlan;
    String            issueKind;
    LocalDateTime     placementDate;
    boolean           amortizationFlag;
    boolean           perpetualFlag;
    long              issueSize;
    boolean           floatingCouponFlag;
}
