package tinkoff.trading.bot.market.mapper;

import org.reactivestreams.Publisher;
import ru.tinkoff.piapi.core.InstrumentsService;
import tinkoff.trading.bot.market.instrument.TradingInstrument;

import java.util.function.BiFunction;

public interface TradingInstrumentCallMapper
        extends BiFunction<InstrumentsService, RequesterObject, Publisher<? extends TradingInstrument>> {
}
