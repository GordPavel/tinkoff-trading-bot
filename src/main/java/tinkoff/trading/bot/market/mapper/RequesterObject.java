package tinkoff.trading.bot.market.mapper;

import lombok.AllArgsConstructor;
import lombok.Value;

import static lombok.AccessLevel.PRIVATE;

@Value
@AllArgsConstructor(access = PRIVATE)
public class RequesterObject {
    RequestInstrumentsType requestType;
    String                 figi;

    public static RequesterObject single(String figi) {
        return new RequesterObject(null, figi);
    }

    public static RequesterObject multiple(RequestInstrumentsType requestType) {
        return new RequesterObject(requestType, null);
    }
}
