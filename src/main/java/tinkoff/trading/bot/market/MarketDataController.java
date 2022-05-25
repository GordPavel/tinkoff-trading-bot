package tinkoff.trading.bot.market;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.tinkoff.piapi.contract.v1.CandleInterval;
import tinkoff.trading.bot.backend.api.model.market.MarketDataMapper;
import tinkoff.trading.bot.backend.api.model.market.model.BackendMarketDataDto;
import tinkoff.trading.bot.backend.api.model.market.model.BackendOrderBookResponse;
import tinkoff.trading.bot.backend.api.model.market.model.BackendTrade;
import tinkoff.trading.bot.backend.api.model.market.model.BackendTradingStatus;
import tinkoff.trading.bot.utils.CompletableFutureToMonoAdapter;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.Set;

import static java.util.function.Function.identity;
import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME;
import static tinkoff.trading.bot.backend.api.SaveInvestApiToReactorContextConfiguration.GET_INVEST_API_FROM_CONTEXT;
import static tinkoff.trading.bot.utils.CompletableFutureToMonoAdapter.toMono;

@RestController
@RequestMapping("/backend/market/data")
@RequiredArgsConstructor
public class MarketDataController {
    private final MarketDataMapper marketDataMapper;

    @Value(("${internal.params.home.time.zone}"))
    private ZoneId homeZoneId;

    @GetMapping("/{figi}/candles")
    public Flux<BackendMarketDataDto> getCandlesForFigi(
            @PathVariable String figi,
            @RequestParam
            @DateTimeFormat(iso = DATE_TIME)
            OffsetDateTime from,
            @RequestParam
            @DateTimeFormat(iso = DATE_TIME)
            OffsetDateTime to,
            @RequestParam
            CandleInterval candleInterval
    ) {
        return GET_INVEST_API_FROM_CONTEXT
                .flatMap(api -> toMono(api.getMarketDataService().getCandles(
                        figi,
                        from.atZoneSameInstant(homeZoneId).toInstant(),
                        to.atZoneSameInstant(homeZoneId).toInstant(),
                        candleInterval
                )))
                .flatMapIterable(identity())
                .map(marketDataMapper::toDto);
    }

    @GetMapping("/{figi}/status")
    public Mono<BackendTradingStatus> getTradingStatusForFigi(
            @PathVariable String figi
    ) {
        return GET_INVEST_API_FROM_CONTEXT
                .flatMap(api -> toMono(api.getMarketDataService().getTradingStatus(figi)))
                .map(marketDataMapper::toDto);
    }

    @GetMapping("/{figi}/last-trades")
    public Flux<BackendTrade> getLastTradesForFigi(
            @PathVariable String figi,
            @RequestParam
            @DateTimeFormat(iso = DATE_TIME)
            Optional<OffsetDateTime> from,
            @RequestParam
            @DateTimeFormat(iso = DATE_TIME)
            Optional<OffsetDateTime> to
    ) {
        return GET_INVEST_API_FROM_CONTEXT
                .flatMap(api -> Mono.justOrEmpty(from).zipWith(Mono.justOrEmpty(to))
                                    .map(times -> api.getMarketDataService().getLastTrades(
                                            figi,
                                            times.getT1().atZoneSameInstant(homeZoneId).toInstant(),
                                            times.getT2().atZoneSameInstant(homeZoneId).toInstant()
                                    ))
                                    .switchIfEmpty(
                                            Mono.fromCallable(() -> api.getMarketDataService().getLastTrades(figi))
                                    )
                                    .flatMap(CompletableFutureToMonoAdapter::toMono)
                )
                .flatMapIterable(identity())
                .map(marketDataMapper::toDto);
    }

    @GetMapping("/{figi}/order-book")
    public Mono<BackendOrderBookResponse> getOrderBookForFigi(
            @PathVariable String figi,
            @RequestParam int depth
    ) {
        return GET_INVEST_API_FROM_CONTEXT
                .flatMap(api -> Mono.fromFuture(api.getMarketDataService().getOrderBook(figi, depth)))
                .map(marketDataMapper::toDto);
    }

    @GetMapping("/last-prices/")
    public Flux<BackendMarketDataDto> getLastPricesForFigis(
            @RequestParam Set<String> figis
    ) {
        return GET_INVEST_API_FROM_CONTEXT
                .flatMap(api -> toMono(api.getMarketDataService().getLastPrices(figis)))
                .flatMapIterable(identity())
                .map(marketDataMapper::toDto);
    }

}
