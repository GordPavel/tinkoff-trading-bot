package tinkoff.trading.bot.account.client;

import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.tinkoff.piapi.contract.v1.OperationState;
import ru.tinkoff.piapi.core.InvestApi;
import tinkoff.trading.bot.account.model.BackendPortfolioResponse;
import tinkoff.trading.bot.account.model.BackendPositionsResponse;
import tinkoff.trading.bot.backend.api.model.BackendAccount;
import tinkoff.trading.bot.backend.api.model.BackendMoneyValue;
import tinkoff.trading.bot.backend.api.model.BackendOperation;

import java.time.OffsetDateTime;

public interface BackendSandboxAccountClient {
    Mono<? extends String> createAccount(InvestApi api);

    Mono<? extends Void> deleteAccount(InvestApi api, String accountId);

    Flux<? extends BackendAccount> getAccountsList(InvestApi api);

    Mono<? extends BackendMoneyValue> payIn(
            InvestApi api,
            String accountId,
            BackendMoneyValue amount
    );

    Mono<? extends BackendPortfolioResponse> getPortfolio(InvestApi api, String accountId);

    Mono<? extends BackendPositionsResponse> getPositions(InvestApi api, String accountId);

    Flux<? extends BackendOperation> getOperations(
            InvestApi api,
            String accountId,
            OffsetDateTime from,
            OffsetDateTime to,
            OperationState operationState,
            String figi
    );
}
