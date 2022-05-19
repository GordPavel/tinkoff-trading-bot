package tinkoff.trading.bot.market.instrument;

import lombok.Value;

@Value
public class AssetSecurity {
    String                   extCase;
    String                   isin;
    String                   type;
    AssetShare               share;
    AssetBond                bond;
    AssetStructuredProduct   sp;
    AssetEtf                 etf;
    AssetClearingCertificate clearingCertificate;
}
