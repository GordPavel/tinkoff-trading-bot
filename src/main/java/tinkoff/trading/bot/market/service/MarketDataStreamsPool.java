package tinkoff.trading.bot.market.service;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalListener;
import lombok.AllArgsConstructor;
import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink.OverflowStrategy;
import reactor.core.publisher.Mono;
import ru.tinkoff.piapi.core.InvestApi;
import tinkoff.trading.bot.backend.api.model.market.MarketDataMapper;
import tinkoff.trading.bot.backend.api.model.market.model.BackendMarketDataDto;
import tinkoff.trading.bot.market.MarketDataStreamToFluxCreator;
import tinkoff.trading.bot.market.SubscriptionType;

import java.time.Duration;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;

@Service
public class MarketDataStreamsPool {

    private final Cache<String, MarketDataStream> streamsPool;
    private final MarketDataMapper                marketDataMapper;
    private final MarketDataStreamsPoolConfigs    configs;

    public MarketDataStreamsPool(MarketDataMapper marketDataMapper, MarketDataStreamsPoolConfigs configs) {
        this.marketDataMapper = marketDataMapper;
        this.configs          = configs;
        this.streamsPool      = CacheBuilder
                .newBuilder()
                .maximumSize(configs.getPoolMaxSize())
                .expireAfterAccess(configs.getPoolExpiresAfter().toMillis(), TimeUnit.MILLISECONDS)
                .removalListener((RemovalListener<String, MarketDataStream>) notification -> {
                    final var stream = getStream(notification.getKey());
                    unsubscribeStream(stream.investApi, notification.getKey());
                })
                .build();
    }

    public String createStream(InvestApi api) {
        final var streamId = UUID.randomUUID().toString();
        final var stream = Flux
                .create(
                        new MarketDataStreamToFluxCreator(streamId, api.getMarketDataStreamService()),
                        configs.getStreamCacheBackpressureStrategy()
                )
                .map(marketDataMapper::toDto)
                .flatMap(Mono::justOrEmpty)
                .cache(configs.getStreamCacheSize(), configs.getStreamCacheExpiresAfter());
        streamsPool.put(streamId, new MarketDataStream(stream, api, new HashSet<>()));
        return streamId;
    }

    public Flux<BackendMarketDataDto> subscribeStream(String streamId) {
        return getStream(streamId).stream;
    }

    public void unsubscribeStream(String streamId) {
        final var stream = getStream(streamId);
        unsubscribeStream(stream.investApi, streamId);
        streamsPool.invalidate(streamId);
    }

    public void addEventsToStream(
            InvestApi api,
            String streamId,
            List<String> figis,
            Set<SubscriptionType> subscriptionTypes
    ) {
        final var streamSubscriptions = getStream(streamId).subscriptions;
        subscriptionTypes.forEach(type -> {
            figis.forEach(figi -> streamSubscriptions.add(new MarketDataSubscription(type, figi)));
            type.subscribe(api.getMarketDataStreamService().getStreamById(streamId), figis);
        });
    }

    public void removeEventsFromStream(
            InvestApi api,
            String streamId,
            List<String> figis,
            Set<SubscriptionType> subscriptionTypes
    ) {
        final var streamSubscriptions = getStream(streamId).subscriptions;
        subscriptionTypes.forEach(type -> {
            figis.forEach(figi -> streamSubscriptions.remove(new MarketDataSubscription(type, figi)));
            type.unsubscribe(api.getMarketDataStreamService().getStreamById(streamId), figis);
        });
    }

    private MarketDataStream getStream(String streamId) {
        return Optional
                .ofNullable(streamsPool.getIfPresent(streamId))
                .orElseThrow(() -> new MarketDataStreamNotFoundException(streamId));
    }

    private void unsubscribeStream(InvestApi api, String streamId) {
        getStream(streamId)
                .subscriptions
                .stream()
                .collect(groupingBy(
                        MarketDataSubscription::getSubscriptionType,
                        mapping(MarketDataSubscription::getFigi, toList())
                ))
                .forEach((type, figis) -> type.unsubscribe(
                        api.getMarketDataStreamService().getStreamById(streamId),
                        figis
                ));
    }

    @Value
    @ConfigurationProperties("tinkoff.api.market.data.pool")
    @ConstructorBinding
    public static class MarketDataStreamsPoolConfigs {
        int              poolMaxSize;
        Duration         poolExpiresAfter;
        int              streamCacheSize;
        Duration         streamCacheExpiresAfter;
        OverflowStrategy streamCacheBackpressureStrategy;
    }

    @AllArgsConstructor
    private static class MarketDataStream {
        Flux<BackendMarketDataDto>  stream;
        InvestApi                   investApi;
        Set<MarketDataSubscription> subscriptions;
    }

    @Value
    private static class MarketDataSubscription {
        SubscriptionType subscriptionType;
        String           figi;
    }
}
