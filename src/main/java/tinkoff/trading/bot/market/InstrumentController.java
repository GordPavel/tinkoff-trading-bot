package tinkoff.trading.bot.market;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tinkoff.trading.bot.market.client.BackendInstrumentClient;
import tinkoff.trading.bot.market.instrument.Asset;
import tinkoff.trading.bot.market.instrument.AssetFull;
import tinkoff.trading.bot.market.instrument.InstrumentType;
import tinkoff.trading.bot.market.instrument.TradingInstrument;
import tinkoff.trading.bot.market.mapper.RequestInstrumentsType;
import tinkoff.trading.bot.market.mapper.RequesterObject;

import static tinkoff.trading.bot.backend.api.SaveInvestApiToReactorContextConfiguration.GET_INVEST_API_FROM_CONTEXT;

@RestController
@RequestMapping("/backend/instruments")
@RequiredArgsConstructor
@Slf4j
public class InstrumentController {

    private final BackendInstrumentClient instrumentClient;

    @GetMapping("/assets")
    public Flux<Asset> getAssets(

    ) {
        return GET_INVEST_API_FROM_CONTEXT
                .flatMapMany(instrumentClient::getAssets);
    }

    @GetMapping("/assets/{uid}")
    public Mono<AssetFull> getAsset(
            @PathVariable String uid
    ) {
        return GET_INVEST_API_FROM_CONTEXT
                .flatMap(api -> instrumentClient.getAsset(api, uid));
    }

    @GetMapping("/{type}/{requestType}")
    public Flux<? extends TradingInstrument> getTradingInstruments(
            @PathVariable InstrumentType type,
            @PathVariable RequestInstrumentsType requestType
    ) {
        return GET_INVEST_API_FROM_CONTEXT
                .flatMapMany(api -> instrumentClient.getTradingInstruments(
                        api,
                        type,
                        RequesterObject.multiple(requestType)
                ));
    }

    @GetMapping("/{type}/figis/{figi}")
    public Flux<? extends TradingInstrument> getTradingSpecifiedInstrument(
            @PathVariable InstrumentType type,
            @PathVariable String figi
    ) {
        return GET_INVEST_API_FROM_CONTEXT
                .flatMapMany(api -> instrumentClient.getTradingInstruments(api, type, RequesterObject.single(figi)));
    }

    @GetMapping("/{figi}")
    public Mono<TradingInstrument> getTradingInstrument(
            @PathVariable String figi
    ) {
        return GET_INVEST_API_FROM_CONTEXT
                .flatMap(api -> instrumentClient.getTradingInstrument(api, figi));
    }

}
