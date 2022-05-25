package tinkoff.trading.bot.market.client;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.tinkoff.piapi.core.InvestApi;
import tinkoff.trading.bot.market.instrument.Asset;
import tinkoff.trading.bot.market.instrument.AssetFull;
import tinkoff.trading.bot.market.instrument.InstrumentType;
import tinkoff.trading.bot.market.instrument.TradingInstrument;
import tinkoff.trading.bot.market.mapper.RequesterObject;

public interface BackendInstrumentClient {
    Flux<? extends Asset> getAssets(InvestApi api);

    Mono<? extends AssetFull> getAsset(InvestApi api, String uid);

    Flux<? extends TradingInstrument> getTradingInstruments(
            InvestApi api,
            InstrumentType type,
            RequesterObject multiple
    );

    Mono<? extends TradingInstrument> getTradingInstrument(InvestApi api, String figi);
}
