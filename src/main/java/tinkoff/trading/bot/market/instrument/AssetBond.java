package tinkoff.trading.bot.market.instrument;

import lombok.Value;
import tinkoff.trading.bot.utils.mappers.backend.BackendMoneyValue;
import tinkoff.trading.bot.utils.mappers.backend.BackendQuotation;

import java.time.LocalDateTime;

@Value
public class AssetBond {
    BackendMoneyValue nominal;
    LocalDateTime     maturityDate;
    BackendQuotation  issueSize;
    String            issueKind;
    int               couponQuantityPerYear;
    boolean           amortizationFlag;
    boolean           floatingCouponFlag;
    boolean           perpetualFlag;
    LocalDateTime     stateRegDate;
    LocalDateTime     placementDate;
    BackendMoneyValue placementPrice;
    BackendQuotation  issueSizePlan;

    BackendQuotation currentNominal;
    String           borrowName;
    String           nominalCurrency;
    String           interestKind;
    boolean          indexedNominalFlag;
    boolean          subordinatedFlag;
    boolean          collateralFlag;
    boolean          taxFreeFlag;
    String           returnCondition;
}
