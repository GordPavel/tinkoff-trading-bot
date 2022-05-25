package tinkoff.trading.bot.account.client.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.tinkoff.piapi.core.InvestApi;
import tinkoff.trading.bot.account.client.BackendUserClient;
import tinkoff.trading.bot.account.model.BackendAccountMapper;
import tinkoff.trading.bot.account.model.BackendInfo;
import tinkoff.trading.bot.account.model.BackendMarginAttributes;
import tinkoff.trading.bot.account.model.BackendTariff;
import tinkoff.trading.bot.backend.api.model.BackendAccount;
import tinkoff.trading.bot.backend.api.model.BackendTypesMapper;

import static java.util.function.Function.identity;
import static tinkoff.trading.bot.utils.CompletableFutureToMonoAdapter.toMono;

@Service
@RequiredArgsConstructor
public class SimpleBackendUserClient
        implements BackendUserClient {

    private final BackendAccountMapper backendAccountMapper;
    private final BackendTypesMapper   backendTypesMapper;


    public Mono<BackendInfo> getInfo(InvestApi api) {
        return toMono(api.getUserService().getInfo())
                .map(backendAccountMapper::toDto);
    }

    public Mono<BackendTariff> getTariff(InvestApi api) {
        return toMono(api.getUserService().getUserTariff())
                .map(backendAccountMapper::toDto);
    }

    public Flux<BackendAccount> getAllAccounts(InvestApi api) {
        return toMono(api.getUserService().getAccounts())
                .flatMapIterable(identity())
                .map(backendTypesMapper::toDto);
    }

    public Mono<BackendMarginAttributes> getAccountMarginAttribute(InvestApi api, String accountId) {
        return toMono(api.getUserService().getMarginAttributes(accountId)).map(backendAccountMapper::toDto);
    }
}
