package tinkoff.trading.bot.account.client;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.tinkoff.piapi.core.InvestApi;
import tinkoff.trading.bot.account.model.BackendDividendsForeignIssuerReport;
import tinkoff.trading.bot.account.model.BackendPortfolioResponse;
import tinkoff.trading.bot.account.model.BackendPositionsResponse;
import tinkoff.trading.bot.account.model.BackendWithdrawLimits;
import tinkoff.trading.bot.backend.api.model.BackendBrokerReport;
import tinkoff.trading.bot.backend.api.model.BackendOperation;

import java.time.OffsetDateTime;
import java.util.Optional;

public interface BackendOperationsClient {
    Flux<BackendOperation> getAllOperations(
            InvestApi api,
            String accountId,
            Optional<String> figi,
            OffsetDateTime from,
            OffsetDateTime to
    );

    Mono<BackendPortfolioResponse> getPortfolio(InvestApi api, String accountId);

    Mono<BackendPositionsResponse> getPositions(InvestApi api, String accountId);

    Flux<BackendBrokerReport> getBrokerReport(
            InvestApi api,
            String accountId,
            int pagesLimit,
            OffsetDateTime from,
            OffsetDateTime to
    );

    Flux<BackendDividendsForeignIssuerReport> getDividendsForeignIssuerReport(
            InvestApi api,
            String accountId,
            int pagesLimit,
            OffsetDateTime from,
            OffsetDateTime to
    );

    Mono<BackendWithdrawLimits> getWithdrawLimits(InvestApi api, String accountId);

    Flux<BackendOperation> getExecutedOperations(
            InvestApi api,
            String accountId,
            OffsetDateTime from,
            OffsetDateTime to,
            Optional<String> figi
    );

    Flux<BackendOperation> getCancelledOperations(
            InvestApi api,
            String accountId,
            OffsetDateTime from,
            OffsetDateTime to,
            Optional<String> figi
    );
}
