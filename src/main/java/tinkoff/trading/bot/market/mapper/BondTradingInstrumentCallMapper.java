package tinkoff.trading.bot.market.mapper;

import lombok.RequiredArgsConstructor;
import org.reactivestreams.Publisher;
import org.springframework.stereotype.Service;
import ru.tinkoff.piapi.core.InstrumentsService;
import tinkoff.trading.bot.market.instrument.Bond;

import static java.util.Objects.isNull;
import static java.util.function.Function.identity;
import static tinkoff.trading.bot.utils.CompletableFutureToMonoAdapter.toMono;

@Service("BOND")
@RequiredArgsConstructor
class BondTradingInstrumentCallMapper
        implements TradingInstrumentCallMapper {

    private final TradingInstrumentsMapper instrumentsMapper;

    @Override
    public Publisher<Bond> apply(InstrumentsService instrumentsService, RequesterObject requesterObject) {
        if (isNull(requesterObject.getFigi())) {
            switch (requesterObject.getRequestType()) {
                case ALL:
                    return toMono(instrumentsService.getAllBonds())
                            .flatMapIterable(identity())
                            .map(instrumentsMapper::toDto);
                case TRADABLE:
                    return toMono(instrumentsService.getTradableBonds())
                            .flatMapIterable(identity())
                            .map(instrumentsMapper::toDto);
            }
        }
        return toMono(instrumentsService.getBondByFigi(requesterObject.getFigi()))
                .map(instrumentsMapper::toDto);
    }
}
