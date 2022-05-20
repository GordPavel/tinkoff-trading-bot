package tinkoff.trading.bot.utils.mappers.backend.market.model;

import lombok.Value;

@Value
public class BackendTradingStatus {
    String  getFigi;
    String  getTradingStatus;
    boolean getLimitOrderAvailableFlag;
    boolean getMarketOrderAvailableFlag;
    boolean getApiTradeAvailableFlag;
}
