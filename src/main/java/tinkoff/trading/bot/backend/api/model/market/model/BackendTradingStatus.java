package tinkoff.trading.bot.backend.api.model.market.model;

import lombok.Value;

@Value
public class BackendTradingStatus {
    String  figi;
    String  tradingStatus;
    boolean limitOrderAvailableFlag;
    boolean marketOrderAvailableFlag;
    boolean apiTradeAvailableFlag;
}
