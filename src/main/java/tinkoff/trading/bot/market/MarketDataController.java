package tinkoff.trading.bot.market;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
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
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static java.util.function.Function.identity;
import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME;
import static org.springframework.http.MediaType.APPLICATION_NDJSON_VALUE;
import static reactor.core.publisher.FluxSink.OverflowStrategy.BUFFER;
import static tinkoff.trading.bot.backend.api.SaveInvestApiToReactorContextConfiguration.GET_INVEST_API_FROM_CONTEXT;
import static tinkoff.trading.bot.utils.CompletableFutureToMonoAdapter.toMono;

@RestController
@RequestMapping("/backend/market/data")
@RequiredArgsConstructor
public class MarketDataController {
    private final MarketDataMapper marketDataMapper;

    @Value(("${internal.params.home.time.zone}"))
    private ZoneId homeZoneId;

    @GetMapping(
            value = "/stream",
            produces = APPLICATION_NDJSON_VALUE
    )
    public Flux<BackendMarketDataDto> getMarketDataStream(
            @RequestParam(value = "subscription-types", defaultValue = "CANDLES_ONE_MINUTE")
            Set<SubscriptionType> subscriptionTypes,
            @RequestBody List<String> figis
    ) {
        final var streamId = UUID.randomUUID().toString();
        return GET_INVEST_API_FROM_CONTEXT
                .flatMapMany(api -> Flux.create(
                        new MarketDataStreamToFluxCreator(
                                streamId,
                                api.getMarketDataStreamService(),
                                figis,
                                subscriptionTypes
                        ),
                        BUFFER
                ))
                .flatMap(response -> Mono.justOrEmpty(marketDataMapper.toDto(response)));
    }

    @GetMapping(
            value = "/{figi}/candle",
            produces = APPLICATION_NDJSON_VALUE
    )
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

    @GetMapping(
            value = "/{figi}/status",
            produces = APPLICATION_NDJSON_VALUE
    )
    public Mono<BackendTradingStatus> getTradingStatusForFigi(
            @PathVariable String figi
    ) {
        return GET_INVEST_API_FROM_CONTEXT
                .flatMap(api -> toMono(api.getMarketDataService().getTradingStatus(figi)))
                .map(marketDataMapper::toDto);
    }

    @GetMapping(
            value = "/{figi}/last-trades",
            produces = APPLICATION_NDJSON_VALUE
    )
    public Flux<BackendTrade> getCandlesForFigi(
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

    @GetMapping(
            value = "/{figi}/last-trades",
            produces = APPLICATION_NDJSON_VALUE
    )
    public Mono<BackendOrderBookResponse> getCandlesForFigi(
            @PathVariable String figi,
            @RequestParam int depth
    ) {
        return GET_INVEST_API_FROM_CONTEXT
                .flatMap(api -> Mono.fromFuture(api.getMarketDataService().getOrderBook(figi, depth)))
                .map(marketDataMapper::toDto);
    }

    @GetMapping(
            value = "/last-prices/",
            produces = APPLICATION_NDJSON_VALUE
    )
    public Flux<BackendMarketDataDto> getLastPricesForFigis(
            @RequestParam Set<String> figis
    ) {
        return GET_INVEST_API_FROM_CONTEXT
                .flatMap(api -> toMono(api.getMarketDataService().getLastPrices(figis)))
                .flatMapIterable(identity())
                .map(marketDataMapper::toDto);
    }


}
