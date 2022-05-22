package tinkoff.trading.bot.backend.api.model.market;

import org.mapstruct.Mapper;
import ru.tinkoff.piapi.contract.v1.Candle;
import ru.tinkoff.piapi.contract.v1.GetOrderBookResponse;
import ru.tinkoff.piapi.contract.v1.GetTradingStatusResponse;
import ru.tinkoff.piapi.contract.v1.HistoricCandle;
import ru.tinkoff.piapi.contract.v1.LastPrice;
import ru.tinkoff.piapi.contract.v1.MarketDataResponse;
import ru.tinkoff.piapi.contract.v1.OrderBook;
import ru.tinkoff.piapi.contract.v1.Trade;
import tinkoff.trading.bot.backend.api.model.BackendTypesMapper;
import tinkoff.trading.bot.backend.api.model.market.model.BackendCandle;
import tinkoff.trading.bot.backend.api.model.market.model.BackendHistoricCandle;
import tinkoff.trading.bot.backend.api.model.market.model.BackendLastPrice;
import tinkoff.trading.bot.backend.api.model.market.model.BackendMarketDataDto;
import tinkoff.trading.bot.backend.api.model.market.model.BackendOrderBook;
import tinkoff.trading.bot.backend.api.model.market.model.BackendOrderBookResponse;
import tinkoff.trading.bot.backend.api.model.market.model.BackendTrade;
import tinkoff.trading.bot.backend.api.model.market.model.BackendTradingStatus;

import java.util.Optional;

@Mapper(
        componentModel = "spring",
        uses = {
                BackendTypesMapper.class,
        }
)
public interface MarketDataMapper {

    default Optional<BackendMarketDataDto> toDto(MarketDataResponse response) {
        if (response.hasCandle()) {
            return Optional.of(toDto(response.getCandle()));
        }
        if (response.hasOrderbook()) {
            return Optional.of(toDto(response.getOrderbook()));
        }
        if (response.hasLastPrice()) {
            return Optional.of(toDto(response.getLastPrice()));
        }
        if (response.hasTrade()) {
            return Optional.of(toDto(response.getTrade()));
        }
        return Optional.empty();
    }

    BackendCandle toDto(Candle candle);

    BackendHistoricCandle toDto(HistoricCandle candle);

    BackendTrade toDto(Trade trade);

    BackendLastPrice toDto(LastPrice lastPrice);

    BackendOrderBook toDto(OrderBook orderBook);

    BackendTradingStatus toDto(GetTradingStatusResponse tradingStatus);

    BackendOrderBookResponse toDto(GetOrderBookResponse orderBook);

}
