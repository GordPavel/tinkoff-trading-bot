package tinkoff.trading.bot.market.service;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@ResponseStatus(NOT_FOUND)
public class MarketDataStreamNotFoundException
        extends IllegalArgumentException {
    public MarketDataStreamNotFoundException(String streamId) {
        super(String.format("Cannot find stream with id %s", streamId));
    }
}
