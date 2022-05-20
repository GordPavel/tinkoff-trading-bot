package tinkoff.trading.bot.market.mapper;

import lombok.RequiredArgsConstructor;
import org.reactivestreams.Publisher;
import org.springframework.stereotype.Service;
import ru.tinkoff.piapi.core.InstrumentsService;
import tinkoff.trading.bot.market.instrument.Future;

import static java.util.Objects.isNull;
import static java.util.function.Function.identity;
import static tinkoff.trading.bot.utils.CompletableFutureToMonoAdapter.toMono;

@Service("FUTURE")
@RequiredArgsConstructor
class FutureTradingInstrumentCallMapper
        implements TradingInstrumentCallMapper {

    private final TradingInstrumentsMapper instrumentsMapper;

    @Override
    public Publisher<Future> apply(InstrumentsService instrumentsService, RequesterObject requesterObject) {
        if (isNull(requesterObject.getFigi())) {
            switch (requesterObject.getRequestType()) {
                case ALL:
                    return toMono(instrumentsService.getAllFutures())
                            .flatMapIterable(identity())
                            .map(instrumentsMapper::toDto);
                case TRADABLE:
                    return toMono(instrumentsService.getTradableFutures())
                            .flatMapIterable(identity())
                            .map(instrumentsMapper::toDto);
            }
        }
        return toMono(instrumentsService.getFutureByFigi(requesterObject.getFigi()))
                .map(instrumentsMapper::toDto);
    }
}
