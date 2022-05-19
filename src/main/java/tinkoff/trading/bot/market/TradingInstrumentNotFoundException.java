package tinkoff.trading.bot.market;

import org.springframework.web.bind.annotation.ResponseStatus;
import tinkoff.trading.bot.market.instrument.InstrumentType;

import static java.lang.String.format;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ResponseStatus(NOT_FOUND)
public class TradingInstrumentNotFoundException
        extends IllegalArgumentException {
    public TradingInstrumentNotFoundException(InstrumentType type, String figi) {
        super(format("Trading instrument of type %s with figi %s not found", type.name(), figi));
    }
}
