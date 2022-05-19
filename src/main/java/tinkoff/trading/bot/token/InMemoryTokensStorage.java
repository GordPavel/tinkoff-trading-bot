package tinkoff.trading.bot.token;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tinkoff.trading.bot.token.exception.AccountNotFoundException;
import tinkoff.trading.bot.token.exception.ExistingTokenException;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static java.util.UUID.randomUUID;
import static java.util.function.Predicate.isEqual;

@Service
public class InMemoryTokensStorage
        implements TokensStorage {

    public final Map<UUID, TokenAccount> accounts = new HashMap<>();

    @Override
    public Mono<UUID> saveAccount(String token) {
        return Flux.fromIterable(accounts.values())
                   .map(TokenAccount::getToken)
                   .any(isEqual(token))
                   .flatMap(existingToken -> {
                       if (existingToken) {
                           return Mono.error(new ExistingTokenException());
                       }
                       final var account = new TokenAccount(randomUUID(), token);
                       accounts.put(account.getId(), account);
                       return Mono.just(account.getId());
                   });
    }

    @Override
    public Mono<TokenAccount> getAccount(UUID id) {
        return Mono.justOrEmpty(accounts.get(id));
    }

    @Override
    public Mono<Void> deleteAccount(UUID id) {
        return Mono.justOrEmpty(accounts.remove(id))
                   .then()
                   .switchIfEmpty(Mono.error(() -> new AccountNotFoundException(id)));
    }
}
