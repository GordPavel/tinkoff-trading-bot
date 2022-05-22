package tinkoff.trading.bot.market.instrument;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;
import tinkoff.trading.bot.backend.api.model.BackendQuotation;

@Data
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = Share.class, name = "Share"),
                @JsonSubTypes.Type(value = Bond.class, name = "Bond"),
                @JsonSubTypes.Type(value = Future.class, name = "Future"),
                @JsonSubTypes.Type(value = Etf.class, name = "ETF"),
                @JsonSubTypes.Type(value = Currency.class, name = "Currency"),
        }
)
public class TradingInstrument {
    InstrumentType instrumentType;
    String         figi;
    String         ticker;
    String         uid;
    String         name;
    String         exchange;
    String         currency;

    String           tradingStatus;
    boolean          sellAvailableFlag;
    boolean          buyAvailableFlag;
    boolean          shortEnabledFlag;
    boolean          apiTradeAvailableFlag;
    String           countryOfRisk;
    String           realExchange;
    BackendQuotation minPriceIncrement;

    int              lot;
    BackendQuotation dshortMin;
    BackendQuotation dlongMin;

    String classCode;
    String otcFlag;

    BackendQuotation dlong;
    BackendQuotation dshort;
    BackendQuotation kshort;
    BackendQuotation klong;
}
