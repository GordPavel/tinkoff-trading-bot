package tinkoff.trading.bot.backend.api.model.market.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = BackendCandle.class, name = "Candle"),
                @JsonSubTypes.Type(value = BackendHistoricCandle.class, name = "HistoricCandle"),
                @JsonSubTypes.Type(value = BackendLastPrice.class, name = "LastPrice"),
                @JsonSubTypes.Type(value = BackendOrderBook.class, name = "OrderBook"),
                @JsonSubTypes.Type(value = BackendTrade.class, name = "Trade"),
        }
)
public interface BackendMarketDataDto {
}
