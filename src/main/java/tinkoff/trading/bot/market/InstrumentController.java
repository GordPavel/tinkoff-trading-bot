package tinkoff.trading.bot.market;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tinkoff.trading.bot.market.instrument.Asset;
import tinkoff.trading.bot.market.instrument.AssetFull;
import tinkoff.trading.bot.market.instrument.InstrumentType;
import tinkoff.trading.bot.market.instrument.TradingInstrument;
import tinkoff.trading.bot.market.mapper.RequestInstrumentsType;
import tinkoff.trading.bot.market.mapper.RequesterObject;
import tinkoff.trading.bot.market.mapper.TradingInstrumentCallMapper;
import tinkoff.trading.bot.market.mapper.TradingInstrumentsMapper;

import java.util.Map;

import static java.util.function.Function.identity;
import static tinkoff.trading.bot.backend.api.SaveInvestApiToReactorContextConfiguration.GET_INVEST_API_FROM_CONTEXT;
import static tinkoff.trading.bot.utils.CompletableFutureToMonoAdapter.toMono;

@RestController
@RequestMapping("/backend/instruments")
@RequiredArgsConstructor
@Slf4j
public class InstrumentController {

    private final Map<String, TradingInstrumentCallMapper> tradingInstrumentCallers;
    private final TradingInstrumentsMapper                 instrumentsMapper;

    @GetMapping("/assets")
    public Flux<Asset> getAssets(

    ) {
        return GET_INVEST_API_FROM_CONTEXT
                .flatMap(api -> toMono(api.getInstrumentsService().getAssets()))
                .flatMapIterable(identity())
                .map(instrumentsMapper::toDto);
    }

    @GetMapping("/assets/{uid}")
    public Mono<AssetFull> getAssets(
            @PathVariable String uid
    ) {
        return GET_INVEST_API_FROM_CONTEXT
                .flatMap(api -> toMono(api.getInstrumentsService().getAssetBy(uid)))
                .map(instrumentsMapper::toDto)
                .onErrorStop();
    }

    @GetMapping("/{type}/{requestType}")
    public Flux<? extends TradingInstrument> getTradingInstruments(
            @PathVariable InstrumentType type,
            @PathVariable RequestInstrumentsType requestType
    ) {
        return GET_INVEST_API_FROM_CONTEXT
                .flatMapMany(api -> tradingInstrumentCallers
                        .get(type.name())
                        .apply(api.getInstrumentsService(), RequesterObject.multiple(requestType))
                );
    }

    @GetMapping("/{type}/figis/{figi}")
    public Flux<? extends TradingInstrument> getTradingSpecifiedInstrumentByFigi(
            @PathVariable InstrumentType type,
            @PathVariable String figi
    ) {
        return GET_INVEST_API_FROM_CONTEXT
                .flatMapMany(api -> tradingInstrumentCallers
                        .get(type.name())
                        .apply(api.getInstrumentsService(), RequesterObject.single(figi))
                )
                .switchIfEmpty(Mono.error(() -> new TradingInstrumentNotFoundException(type, figi)));
    }

    @GetMapping("/{figi}")
    public Flux<TradingInstrument> getTradingInstrumentByFigi(
            @PathVariable String figi
    ) {
        return GET_INVEST_API_FROM_CONTEXT
                .flatMapMany(api -> toMono(api.getInstrumentsService().getInstrumentByFigi(figi)))
                .map(instrumentsMapper::toDto);
    }

}
