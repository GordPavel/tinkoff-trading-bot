package tinkoff.trading.bot.market.instrument;

import lombok.Value;
import tinkoff.trading.bot.utils.mappers.backend.BackendQuotation;

import java.time.LocalDateTime;
import java.util.List;

@Value
public class AssetEtf {
    String paymentType;
    String rebalancingFreq;
    String managementType;
    String primaryIndex;
    String focusType;
    String description;
    String primaryIndexDescription;
    String primaryIndexCompany;
    String inavCode;
    String rebalancingPlan;
    String taxRate;
    String issueKind;
    String nominalCurrency;

    boolean watermarkFlag;
    boolean rebalancingFlag;
    boolean ucitsFlag;
    boolean divYieldFlag;
    boolean leveragedFlag;

    LocalDateTime releasedDate;

    BackendQuotation    totalExpense;
    BackendQuotation    hurdleRate;
    BackendQuotation    performanceFee;
    BackendQuotation    fixedCommission;
    BackendQuotation    buyPremium;
    BackendQuotation    sellDiscount;
    BackendQuotation    numShare;
    BackendQuotation    indexRecoveryPeriod;
    BackendQuotation    expenseCommission;
    BackendQuotation    primaryIndexTrackingError;
    List<LocalDateTime> rebalancingDatesList;
    BackendQuotation    nominal;
}
