package tinkoff.trading.bot.market.instrument;

import lombok.Value;
import tinkoff.trading.bot.backend.api.model.BackendQuotation;

@Value
public class AssetClearingCertificate {
    BackendQuotation nominal;
    String           nominalCurrency;
}
