package tinkoff.trading.bot.account.client.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.tinkoff.piapi.contract.v1.BrokerReportResponse;
import ru.tinkoff.piapi.contract.v1.GenerateBrokerReportResponse;
import ru.tinkoff.piapi.contract.v1.GenerateDividendsForeignIssuerReportResponse;
import ru.tinkoff.piapi.contract.v1.GetBrokerReportResponse;
import ru.tinkoff.piapi.contract.v1.GetDividendsForeignIssuerReportResponse;
import ru.tinkoff.piapi.contract.v1.GetDividendsForeignIssuerResponse;
import ru.tinkoff.piapi.core.InvestApi;
import ru.tinkoff.piapi.core.OperationsService;
import tinkoff.trading.bot.account.client.BackendOperationsClient;
import tinkoff.trading.bot.account.model.BackendAccountMapper;
import tinkoff.trading.bot.account.model.BackendDividendsForeignIssuerReport;
import tinkoff.trading.bot.account.model.BackendPortfolioResponse;
import tinkoff.trading.bot.account.model.BackendPositionsResponse;
import tinkoff.trading.bot.account.model.BackendWithdrawLimits;
import tinkoff.trading.bot.backend.api.model.BackendBrokerReport;
import tinkoff.trading.bot.backend.api.model.BackendOperation;
import tinkoff.trading.bot.backend.api.model.BackendTypesMapper;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Optional;

import static java.lang.Math.min;
import static java.util.function.Function.identity;
import static tinkoff.trading.bot.utils.CompletableFutureToMonoAdapter.toMono;

@Service
@RequiredArgsConstructor
class SimpleBackendOperationsClient
        implements BackendOperationsClient {

    private final BackendTypesMapper   backendTypesMapper;
    private final BackendAccountMapper backendAccountMapper;

    @Value(("${internal.params.home.time.zone}"))
    private ZoneId homeZoneId;

    @Override
    public Flux<BackendOperation> getAllOperations(
            InvestApi api,
            String accountId,
            Optional<String> figi,
            OffsetDateTime from,
            OffsetDateTime to
    ) {
        return toMono(
                figi
                        .map(f -> api.getOperationsService().getAllOperations(
                                accountId,
                                from.atZoneSameInstant(homeZoneId).toInstant(),
                                to.atZoneSameInstant(homeZoneId).toInstant(),
                                f
                        ))
                        .orElseGet(() -> api.getOperationsService().getAllOperations(
                                accountId,
                                from.atZoneSameInstant(homeZoneId).toInstant(),
                                to.atZoneSameInstant(homeZoneId).toInstant()
                        ))
        )
                .flatMapIterable(identity())
                .map(backendTypesMapper::toDto);
    }

    @Override
    public Mono<BackendPortfolioResponse> getPortfolio(InvestApi api, String accountId) {
        return toMono(api.getOperationsService().getPortfolio(accountId)).map(backendAccountMapper::toDto);
    }

    @Override
    public Mono<BackendPositionsResponse> getPositions(InvestApi api, String accountId) {
        return toMono(api.getOperationsService().getPositions(accountId)).map(backendAccountMapper::toDto);
    }

    @Override
    public Flux<BackendBrokerReport> getBrokerReport(
            InvestApi api,
            String accountId,
            int pagesLimit,
            OffsetDateTime from,
            OffsetDateTime to
    ) {
        return getGetBrokerReportResponseFlux(api, accountId, from, to, pagesLimit)
                .flatMapIterable(GetBrokerReportResponse::getBrokerReportList)
                .map(backendAccountMapper::toDto);
    }

    @Override
    public Flux<BackendDividendsForeignIssuerReport> getDividendsForeignIssuerReport(
            InvestApi api,
            String accountId,
            int pagesLimit,
            OffsetDateTime from,
            OffsetDateTime to
    ) {
        return getGetDividendsForeignIssuerReportResponseFlux(
                api,
                accountId,
                from,
                to,
                pagesLimit
        )
                .flatMapIterable(GetDividendsForeignIssuerReportResponse::getDividendsForeignIssuerReportList)
                .map(backendAccountMapper::toDto);
    }

    @Override
    public Mono<BackendWithdrawLimits> getWithdrawLimits(InvestApi api, String accountId) {
        return toMono(api.getOperationsService().getWithdrawLimits(accountId))
                .map(backendAccountMapper::toDto);
    }

    @Override
    public Flux<BackendOperation> getExecutedOperations(
            InvestApi api,
            String accountId,
            OffsetDateTime from,
            OffsetDateTime to,
            Optional<String> figi
    ) {
        final OperationsService operationsService = api.getOperationsService();
        return toMono(
                figi
                        .map(f -> operationsService.getExecutedOperations(
                                accountId,
                                from.atZoneSameInstant(homeZoneId).toInstant(),
                                to.atZoneSameInstant(homeZoneId).toInstant(),
                                f
                        ))
                        .orElseGet(() -> operationsService.getExecutedOperations(
                                accountId,
                                from.atZoneSameInstant(homeZoneId).toInstant(),
                                to.atZoneSameInstant(homeZoneId).toInstant()
                        ))
        )
                .flatMapIterable(identity())
                .map(backendTypesMapper::toDto);
    }

    @Override
    public Flux<BackendOperation> getCancelledOperations(
            InvestApi api,
            String accountId,
            OffsetDateTime from,
            OffsetDateTime to,
            Optional<String> figi
    ) {
        final OperationsService operationsService = api.getOperationsService();
        return toMono(
                figi
                        .map(f -> operationsService.getCancelledOperations(
                                accountId,
                                from.atZoneSameInstant(homeZoneId).toInstant(),
                                to.atZoneSameInstant(homeZoneId).toInstant(),
                                f
                        ))
                        .orElseGet(() -> operationsService.getCancelledOperations(
                                accountId,
                                from.atZoneSameInstant(homeZoneId).toInstant(),
                                to.atZoneSameInstant(homeZoneId).toInstant()
                        ))
        )
                .flatMapIterable(identity())
                .map(backendTypesMapper::toDto);
    }

    private Flux<GetDividendsForeignIssuerReportResponse> getGetDividendsForeignIssuerReportResponseFlux(
            InvestApi api,
            String accountId,
            OffsetDateTime from,
            OffsetDateTime to,
            int pagesLimit
    ) {
        final OperationsService operationsService = api.getOperationsService();
        final Mono<GetDividendsForeignIssuerResponse> firstResponse =
                toMono(operationsService.getDividendsForeignIssuer(
                        accountId,
                        from.atZoneSameInstant(homeZoneId).toInstant(),
                        to.atZoneSameInstant(homeZoneId).toInstant()
                )).cache();

        final var pagesCount = firstResponse
                .map(GetDividendsForeignIssuerResponse::getDivForeignIssuerReport)
                .map(GetDividendsForeignIssuerReportResponse::getPagesCount);

        final var taskId = firstResponse
                .map(GetDividendsForeignIssuerResponse::getGenerateDivForeignIssuerReportResponse)
                .map(GenerateDividendsForeignIssuerReportResponse::getTaskId);

        final Flux<GetDividendsForeignIssuerReportResponse> followingPages = pagesCount
                .zipWith(taskId)
                .flatMapMany(pagesCountWithTaskId -> Flux
                        .range(1, min(pagesLimit - 1, pagesCountWithTaskId.getT1()))
                        .flatMap(page -> Mono.just(pagesCountWithTaskId.getT2()).zipWith(Mono.just(page)))
                        .flatMap(pageWithTaskId -> toMono(operationsService.getDividendsForeignIssuer(
                                pageWithTaskId.getT1(),
                                pageWithTaskId.getT2()
                        )))
                );
        return Flux.concat(
                firstResponse.map(GetDividendsForeignIssuerResponse::getDivForeignIssuerReport),
                followingPages
        );
    }

    private Flux<GetBrokerReportResponse> getGetBrokerReportResponseFlux(
            InvestApi api,
            String accountId,
            OffsetDateTime from,
            OffsetDateTime to,
            int pagesLimit
    ) {
        final OperationsService operationsService = api.getOperationsService();
        final Mono<BrokerReportResponse> firstResponse =
                toMono(operationsService.getBrokerReport(
                        accountId,
                        from.atZoneSameInstant(homeZoneId).toInstant(),
                        to.atZoneSameInstant(homeZoneId).toInstant()
                )).cache();

        final var pagesCount = firstResponse
                .map(BrokerReportResponse::getGetBrokerReportResponse)
                .map(GetBrokerReportResponse::getPagesCount);

        final var taskId = firstResponse
                .map(BrokerReportResponse::getGenerateBrokerReportResponse)
                .map(GenerateBrokerReportResponse::getTaskId);

        final Flux<GetBrokerReportResponse> followingPages = pagesCount
                .zipWith(taskId)
                .flatMapMany(pagesCountWithTaskId -> Flux
                        .range(1, min(pagesLimit - 1, pagesCountWithTaskId.getT1()))
                        .flatMap(page -> Mono.just(pagesCountWithTaskId.getT2()).zipWith(Mono.just(page)))
                        .flatMap(pageWithTaskId -> toMono(operationsService.getBrokerReport(
                                pageWithTaskId.getT1(),
                                pageWithTaskId.getT2()
                        )))
                );
        return Flux.concat(
                firstResponse.map(BrokerReportResponse::getGetBrokerReportResponse),
                followingPages
        );
    }

}
