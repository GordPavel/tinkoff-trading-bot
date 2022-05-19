package tinkoff.trading.bot.market;

import reactor.core.publisher.FluxSink;
import ru.tinkoff.piapi.contract.v1.MarketDataResponse;
import ru.tinkoff.piapi.core.stream.MarketDataStreamService;
import ru.tinkoff.piapi.core.stream.MarketDataSubscriptionService;

import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public class MarketDataStreamToFluxCreator
        implements Consumer<FluxSink<MarketDataResponse>> {

    private final String                  streamId;
    private final MarketDataStreamService marketDataStreamService;
    private final List<String>            figis;
    private final Set<SubscriptionType>   subscriptionTypes;

    public MarketDataStreamToFluxCreator(
            String streamId,
            MarketDataStreamService marketDataStreamService,
            List<String> figis,
            Set<SubscriptionType> subscriptionTypes
    ) {
        this.streamId                = streamId;
        this.marketDataStreamService = marketDataStreamService;
        this.figis                   = figis;
        this.subscriptionTypes       = subscriptionTypes;
    }

    @Override
    public void accept(FluxSink<MarketDataResponse> sink) {
        final MarketDataSubscriptionService
                subscription =
                marketDataStreamService.newStream(streamId, sink::next, sink::error);
        subscribe(subscription);
        sink.onCancel(() -> unsubscribe(subscription));
    }

    public void subscribe(MarketDataSubscriptionService subscription) {
        subscriptionTypes.forEach(subscriptionType -> subscriptionType.subscribe(subscription, figis));
    }

    public void unsubscribe(MarketDataSubscriptionService subscription) {
        subscriptionTypes.forEach(subscriptionType -> subscriptionType.unsubscribe(subscription, figis));
    }
}
