package tinkoff.trading.bot.token.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

import static java.lang.String.format;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ResponseStatus(NOT_FOUND)
public class AccountNotFoundException
        extends IllegalArgumentException {
    public AccountNotFoundException(UUID id) {
        super(format("Account with id %s not found", id.toString()));
    }
}
