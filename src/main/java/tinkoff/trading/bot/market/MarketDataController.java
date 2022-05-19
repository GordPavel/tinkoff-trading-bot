package tinkoff.trading.bot.market;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import ru.tinkoff.piapi.contract.v1.MarketDataResponse;
import tinkoff.trading.bot.utils.mappers.backend.BackendCandle;
import tinkoff.trading.bot.utils.mappers.backend.BackendTypesMapper;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import static tinkoff.trading.bot.backend.api.SaveInvestApiToReactorContextConfiguration.GET_INVEST_API_FROM_CONTEXT;
import static tinkoff.trading.bot.market.SubscriptionType.CANDLES_ONE_MINUTE;

@RestController
@RequestMapping("/backend/market/data")
@RequiredArgsConstructor
public class MarketDataController {
    private final BackendTypesMapper backendTypesMapper;

    @GetMapping("/stream")
    public Flux<BackendCandle> getMarketData(
            @RequestBody List<String> figis
    ) {
        final var streamId = UUID.randomUUID().toString();
        return GET_INVEST_API_FROM_CONTEXT
                .flatMapMany(api -> Flux.create(new MarketDataStreamToFluxCreator(
                        streamId,
                        api.getMarketDataStreamService(),
                        figis,
                        Set.of(CANDLES_ONE_MINUTE)
                )))
                .filter(MarketDataResponse::hasCandle)
                .map(MarketDataResponse::getCandle)
                .map(backendTypesMapper::toDto);
    }
}
