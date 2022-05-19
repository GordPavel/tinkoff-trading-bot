package tinkoff.trading.bot.market.instrument;

import lombok.Value;
import tinkoff.trading.bot.utils.mappers.backend.BackendQuotation;

import java.time.LocalDateTime;

@Value
public class AssetShare {
    String getNominalCurrency;
    String getPrimaryIndex;
    String getPreferredShareType;
    String getIssueKind;
    String getRepresIsin;

    LocalDateTime getIpoDate;
    LocalDateTime getRegistryDate;
    LocalDateTime getPlacementDate;

    boolean getDivYieldFlag;

    String getType;

    BackendQuotation getIssueSize;
    BackendQuotation getIssueSizePlan;
    BackendQuotation getNominal;
    BackendQuotation getDividendRate;
    BackendQuotation getTotalFloat;
}
