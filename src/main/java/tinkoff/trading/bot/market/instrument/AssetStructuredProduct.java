package tinkoff.trading.bot.market.instrument;

import lombok.Value;
import tinkoff.trading.bot.backend.api.model.BackendQuotation;

import java.time.LocalDateTime;

@Value
public class AssetStructuredProduct {
    String getBorrowName;
    String getNominalCurrency;
    String getLogicPortfolio;
    String getBasicAsset;
    String getIssueKind;

    LocalDateTime getMaturityDate;
    LocalDateTime getPlacementDate;

    String getType;
    String getAssetType;

    BackendQuotation getNominal;
    BackendQuotation getSafetyBarrier;
    BackendQuotation getIssueSizePlan;
    BackendQuotation getIssueSize;
}
