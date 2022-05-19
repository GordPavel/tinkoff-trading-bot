package tinkoff.trading.bot.market.instrument;

import lombok.Value;
import tinkoff.trading.bot.utils.mappers.backend.BackendQuotation;

@Value
public class AssetClearingCertificate {
    BackendQuotation nominal;
    String           nominalCurrency;
}
