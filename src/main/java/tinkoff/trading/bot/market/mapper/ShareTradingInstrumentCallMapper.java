package tinkoff.trading.bot.market.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import ru.tinkoff.piapi.core.InstrumentsService;
import tinkoff.trading.bot.market.instrument.Share;

import static java.util.Objects.isNull;
import static java.util.function.Function.identity;
import static tinkoff.trading.bot.utils.CompletableFutureToMonoAdapter.toMono;

@Service("SHARE")
@RequiredArgsConstructor
class ShareTradingInstrumentCallMapper
        implements TradingInstrumentCallMapper {

    private final TradingInstrumentsMapper instrumentsMapper;

    @Override
    public Flux<Share> apply(InstrumentsService instrumentsService, RequesterObject requesterObject) {
        if (isNull(requesterObject.getFigi())) {
            switch (requesterObject.getRequestType()) {
                case ALL:
                    return toMono(instrumentsService.getAllShares())
                            .flatMapIterable(identity())
                            .map(instrumentsMapper::toDto);
                case TRADABLE:
                    return toMono(instrumentsService.getTradableShares())
                            .flatMapIterable(identity())
                            .map(instrumentsMapper::toDto);
            }
        }

        return toMono(instrumentsService.getShareByFigi(requesterObject.getFigi()))
                .map(instrumentsMapper::toDto)
                .flux();
    }
}
