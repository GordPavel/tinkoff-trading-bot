package tinkoff.trading.bot.market.mapper;

import reactor.core.publisher.Flux;
import ru.tinkoff.piapi.core.InstrumentsService;
import tinkoff.trading.bot.market.instrument.TradingInstrument;

import java.util.function.BiFunction;

public interface TradingInstrumentCallMapper
        extends BiFunction<InstrumentsService, RequesterObject, Flux<? extends TradingInstrument>> {
}
