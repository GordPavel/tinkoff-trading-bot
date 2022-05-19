package tinkoff.trading.bot.token.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static tinkoff.trading.bot.backend.api.SaveInvestApiToReactorContextConfiguration.TOKEN_ID_HEADER;

@ResponseStatus(FORBIDDEN)
public class EmptyTokenIdHeaderException
        extends RuntimeException {
    public EmptyTokenIdHeaderException() {
        super(String.format("Request requires %s header with user token id", TOKEN_ID_HEADER));
    }
}

