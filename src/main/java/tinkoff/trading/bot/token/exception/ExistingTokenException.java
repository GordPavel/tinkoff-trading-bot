package tinkoff.trading.bot.token.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.CONFLICT;

@ResponseStatus(CONFLICT)
public class ExistingTokenException
        extends IllegalArgumentException {
    public ExistingTokenException() {
        super("Token already exists in the storage");
    }
}
