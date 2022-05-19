package tinkoff.trading.bot.market.instrument;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Future
        extends TradingInstrument {
    String        futuresType;
    LocalDateTime lastTradeDate;
    LocalDateTime firstTradeDate;
    LocalDateTime expirationDate;
    String        sector;
    String        basicAsset;
    String        assetType;
}
