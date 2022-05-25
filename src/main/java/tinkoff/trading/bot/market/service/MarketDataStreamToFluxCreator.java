package tinkoff.trading.bot.market;

import reactor.core.publisher.FluxSink;
import ru.tinkoff.piapi.contract.v1.MarketDataResponse;
import ru.tinkoff.piapi.core.stream.MarketDataStreamService;

import java.util.function.Consumer;

public class MarketDataStreamToFluxCreator
        implements Consumer<FluxSink<MarketDataResponse>> {

    private final String                  streamId;
    private final MarketDataStreamService marketDataStreamService;

    public MarketDataStreamToFluxCreator(
            String streamId,
            MarketDataStreamService marketDataStreamService
    ) {
        this.streamId                = streamId;
        this.marketDataStreamService = marketDataStreamService;
    }

    @Override
    public void accept(FluxSink<MarketDataResponse> sink) {
        marketDataStreamService.newStream(streamId, sink::next, sink::error);
    }

}
