package tinkoff.trading.bot.market.client.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.tinkoff.piapi.core.InvestApi;
import tinkoff.trading.bot.market.client.BackendInstrumentClient;
import tinkoff.trading.bot.market.instrument.Asset;
import tinkoff.trading.bot.market.instrument.AssetFull;
import tinkoff.trading.bot.market.instrument.InstrumentType;
import tinkoff.trading.bot.market.instrument.TradingInstrument;
import tinkoff.trading.bot.market.mapper.RequesterObject;
import tinkoff.trading.bot.market.mapper.TradingInstrumentCallMapper;
import tinkoff.trading.bot.market.mapper.TradingInstrumentsMapper;

import java.util.Map;

import static java.util.function.Function.identity;
import static tinkoff.trading.bot.utils.CompletableFutureToMonoAdapter.toMono;

@Service
@RequiredArgsConstructor
public class SimpleBackendInstrumentClient
        implements BackendInstrumentClient {

    private final Map<String, TradingInstrumentCallMapper> tradingInstrumentCallers;
    private final TradingInstrumentsMapper                 instrumentsMapper;

    public Mono<TradingInstrument> getTradingInstrument(InvestApi api, String figi) {
        return toMono(api.getInstrumentsService().getInstrumentByFigi(figi))
                .map(instrumentsMapper::toDto);
    }

    public Flux<Asset> getAssets(InvestApi api) {
        return toMono(api.getInstrumentsService().getAssets())
                .flatMapIterable(identity())
                .map(instrumentsMapper::toDto);
    }

    public Mono<AssetFull> getAsset(InvestApi api, String uid) {
        return toMono(api.getInstrumentsService().getAssetBy(uid)).map(instrumentsMapper::toDto);
    }

    public Flux<? extends TradingInstrument> getTradingInstruments(
            InvestApi api,
            InstrumentType type,
            RequesterObject requestType
    ) {
        return tradingInstrumentCallers
                .get(type.name())
                .apply(api.getInstrumentsService(), requestType);
    }
}
