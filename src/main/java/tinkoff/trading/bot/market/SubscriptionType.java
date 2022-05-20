package tinkoff.trading.bot.market;

import ru.tinkoff.piapi.core.stream.MarketDataSubscriptionService;

import java.util.List;
import java.util.function.BiConsumer;

import static ru.tinkoff.piapi.contract.v1.SubscriptionInterval.SUBSCRIPTION_INTERVAL_FIVE_MINUTES;
import static ru.tinkoff.piapi.contract.v1.SubscriptionInterval.SUBSCRIPTION_INTERVAL_ONE_MINUTE;

public enum SubscriptionType {
    TRADES(MarketDataSubscriptionService::subscribeTrades, MarketDataSubscriptionService::unsubscribeTrades),
    LAST_PRICES(
            MarketDataSubscriptionService::subscribeLastPrices,
            MarketDataSubscriptionService::unsubscribeLastPrices
    ),
    ORDER_BOOK(MarketDataSubscriptionService::subscribeOrderbook, MarketDataSubscriptionService::unsubscribeOrderbook),
    CANDLES_ONE_MINUTE(
            (subscriptionService, figis) -> subscriptionService.subscribeCandles(
                    figis,
                    SUBSCRIPTION_INTERVAL_ONE_MINUTE
            ),
            MarketDataSubscriptionService::unsubscribeCandles
    ),
    CANDLES_FIVE_MINUTES(
            (subscriptionService, figis) -> subscriptionService.subscribeCandles(
                    figis,
                    SUBSCRIPTION_INTERVAL_FIVE_MINUTES
            ),
            MarketDataSubscriptionService::unsubscribeCandles
    ),
    ;

    private final BiConsumer<MarketDataSubscriptionService, List<String>> subscribe;
    private final BiConsumer<MarketDataSubscriptionService, List<String>> unsubscribe;

    SubscriptionType(
            BiConsumer<MarketDataSubscriptionService, List<String>> subscribe,
            BiConsumer<MarketDataSubscriptionService, List<String>> unsubscribe
    ) {
        this.subscribe   = subscribe;
        this.unsubscribe = unsubscribe;
    }

    public void subscribe(MarketDataSubscriptionService subscription, List<String> figis) {
        this.subscribe.accept(subscription, figis);
    }

    public void unsubscribe(MarketDataSubscriptionService subscription, List<String> figis) {
        this.unsubscribe.accept(subscription, figis);
    }
}
