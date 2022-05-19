package tinkoff.trading.bot.token;

import reactor.core.publisher.Mono;

import java.util.UUID;

public interface TokensStorage {
    Mono<UUID> saveAccount(String token);

    Mono<TokenAccount> getAccount(UUID id);

    Mono<Void> deleteAccount(UUID id);
}
