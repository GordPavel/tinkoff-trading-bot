package tinkoff.trading.bot.market;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tinkoff.trading.bot.backend.api.model.market.model.BackendMarketDataDto;
import tinkoff.trading.bot.market.service.MarketDataStreamsPool;

import java.util.List;
import java.util.Set;

import static org.springframework.http.MediaType.APPLICATION_NDJSON_VALUE;
import static tinkoff.trading.bot.backend.api.SaveInvestApiToReactorContextConfiguration.GET_INVEST_API_FROM_CONTEXT;

@RestController
@RequestMapping("/backend/market/data/streams")
@RequiredArgsConstructor
public class MarketDataStreamController {
    private final MarketDataStreamsPool streamsPool;

    @PostMapping
    public Mono<String> createStream(

    ) {
        return GET_INVEST_API_FROM_CONTEXT
                .map(streamsPool::createStream);
    }

    @DeleteMapping("/{streamId}")
    public void removeStream(
            @PathVariable String streamId
    ) {
        streamsPool.unsubscribeStream(streamId);
    }

    @GetMapping(
            value = "/{streamId}",
            produces = APPLICATION_NDJSON_VALUE
    )
    public Flux<BackendMarketDataDto> subscribeStream(
            @PathVariable String streamId
    ) {
        return streamsPool.subscribeStream(streamId);
    }

    @PostMapping("/{streamId}/subscription")
    public Mono<Void> addEventsToStream(
            @PathVariable String streamId,
            @RequestParam(value = "subscription-types", defaultValue = "CANDLES_ONE_MINUTE")
            Set<SubscriptionType> subscriptionTypes,
            @RequestBody List<String> figis
    ) {
        return GET_INVEST_API_FROM_CONTEXT
                .mapNotNull(api -> {
                    streamsPool.addEventsToStream(
                            api,
                            streamId,
                            figis,
                            subscriptionTypes
                    );
                    return null;
                });
    }

    @DeleteMapping("/{streamId}/subscription")
    public Mono<Void> removeEventsFromStream(
            @PathVariable String streamId,
            @RequestParam(value = "subscription-types", defaultValue = "CANDLES_ONE_MINUTE")
            Set<SubscriptionType> subscriptionTypes,
            @RequestBody List<String> figis
    ) {
        return GET_INVEST_API_FROM_CONTEXT
                .mapNotNull(api -> {
                    streamsPool.removeEventsFromStream(
                            api,
                            streamId,
                            figis,
                            subscriptionTypes
                    );
                    return null;
                });
    }
}
