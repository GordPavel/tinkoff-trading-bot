package tinkoff.trading.bot.account.client.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.tinkoff.piapi.contract.v1.OperationState;
import ru.tinkoff.piapi.core.InvestApi;
import tinkoff.trading.bot.account.client.BackendSandboxAccountClient;
import tinkoff.trading.bot.account.model.BackendAccountMapper;
import tinkoff.trading.bot.account.model.BackendPortfolioResponse;
import tinkoff.trading.bot.account.model.BackendPositionsResponse;
import tinkoff.trading.bot.backend.api.model.BackendAccount;
import tinkoff.trading.bot.backend.api.model.BackendMoneyValue;
import tinkoff.trading.bot.backend.api.model.BackendOperation;
import tinkoff.trading.bot.backend.api.model.BackendTypesMapper;

import java.time.OffsetDateTime;
import java.time.ZoneId;

import static java.util.function.Function.identity;
import static tinkoff.trading.bot.utils.CompletableFutureToMonoAdapter.toMono;

@Service
@RequiredArgsConstructor
public class SimpleBackendSandboxAccountClient
        implements BackendSandboxAccountClient {

    private final BackendAccountMapper backendAccountMapper;
    private final BackendTypesMapper   backendTypesMapper;

    @Value(("${internal.params.home.time.zone}"))
    private ZoneId homeZoneId;

    public Mono<BackendPositionsResponse> getPositions(InvestApi api, String accountId) {
        return toMono(api.getSandboxService().getPositions(accountId)).map(backendAccountMapper::toDto);
    }

    public Flux<BackendOperation> getOperations(
            InvestApi api,
            String accountId,
            OffsetDateTime from,
            OffsetDateTime to,
            OperationState operationState,
            String figi
    ) {
        return toMono(api.getSandboxService().getOperations(
                accountId,
                from.atZoneSameInstant(homeZoneId).toInstant(),
                to.atZoneSameInstant(homeZoneId).toInstant(),
                operationState,
                figi
        ))
                .flatMapIterable(identity())
                .map(backendTypesMapper::toDto);
    }

    public Mono<BackendPortfolioResponse> getPortfolio(InvestApi api, String accountId) {
        return toMono(api.getSandboxService().getPortfolio(accountId))
                .map(backendAccountMapper::toDto);
    }

    public Mono<BackendMoneyValue> payIn(InvestApi api, String accountId, BackendMoneyValue amount) {
        return toMono(api.getSandboxService().payIn(accountId, backendTypesMapper.fromDto(amount)))
                .map(backendTypesMapper::toDto);
    }

    public Mono<String> createAccount(InvestApi api) {
        return toMono(api.getSandboxService().openAccount());
    }

    public Mono<Void> deleteAccount(InvestApi api, String accountId) {
        return toMono(api.getSandboxService().closeAccount(accountId));
    }

    public Flux<BackendAccount> getAccountsList(InvestApi api) {
        return toMono(api.getSandboxService().getAccounts())
                .flatMapIterable(identity())
                .map(backendTypesMapper::toDto);
    }
}
