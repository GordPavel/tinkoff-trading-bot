package tinkoff.trading.bot.account;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.tinkoff.piapi.contract.v1.BrokerReportResponse;
import ru.tinkoff.piapi.contract.v1.GenerateBrokerReportResponse;
import ru.tinkoff.piapi.contract.v1.GenerateDividendsForeignIssuerReportResponse;
import ru.tinkoff.piapi.contract.v1.GetBrokerReportResponse;
import ru.tinkoff.piapi.contract.v1.GetDividendsForeignIssuerReportResponse;
import ru.tinkoff.piapi.contract.v1.GetDividendsForeignIssuerResponse;
import ru.tinkoff.piapi.contract.v1.Operation;
import ru.tinkoff.piapi.core.InvestApi;
import ru.tinkoff.piapi.core.OperationsService;
import tinkoff.trading.bot.account.model.BackendAccountMapper;
import tinkoff.trading.bot.account.model.BackendDividendsForeignIssuerReport;
import tinkoff.trading.bot.account.model.BackendPortfolioResponse;
import tinkoff.trading.bot.account.model.BackendPositionsResponse;
import tinkoff.trading.bot.account.model.BackendWithdrawLimits;
import tinkoff.trading.bot.backend.api.model.BackendBrokerReport;
import tinkoff.trading.bot.backend.api.model.BackendOperation;
import tinkoff.trading.bot.backend.api.model.BackendTypesMapper;

import javax.validation.ValidationException;
import javax.validation.constraints.Min;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

import static java.lang.Math.min;
import static java.util.function.Function.identity;
import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME;
import static tinkoff.trading.bot.backend.api.SaveInvestApiToReactorContextConfiguration.GET_INVEST_API_FROM_CONTEXT;
import static tinkoff.trading.bot.utils.CompletableFutureToMonoAdapter.toMono;

@RestController
@RequestMapping("/backend/accounts")
@RequiredArgsConstructor
public class OperationsController {
    private final BackendTypesMapper   backendTypesMapper;
    private final BackendAccountMapper backendAccountMapper;

    @Value(("${internal.params.home.time.zone}"))
    private ZoneId homeZoneId;

    @GetMapping("/{accountId}/operations")
    public Flux<BackendOperation> getAllOperations(
            @PathVariable String accountId,
            @RequestParam Optional<String> figi,
            @RequestParam
            @DateTimeFormat(iso = DATE_TIME)
            OffsetDateTime from,
            @RequestParam
            @DateTimeFormat(iso = DATE_TIME)
            OffsetDateTime to
    ) {
        return GET_INVEST_API_FROM_CONTEXT
                .flatMap(api -> toMono(
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
                ))
                .flatMapIterable(identity())
                .map(backendTypesMapper::toDto);
    }

    @GetMapping("/{accountId}/portfolio")
    public Mono<BackendPortfolioResponse> getPortfolio(
            @PathVariable String accountId
    ) {
        return GET_INVEST_API_FROM_CONTEXT
                .flatMap(api -> toMono(api.getOperationsService().getPortfolio(accountId)))
                .map(backendAccountMapper::toDto);
    }

    @GetMapping("/{accountId}/positions")
    public Mono<BackendPositionsResponse> getPositions(
            @PathVariable String accountId
    ) {
        return GET_INVEST_API_FROM_CONTEXT
                .flatMap(api -> toMono(api.getOperationsService().getPositions(accountId)))
                .map(backendAccountMapper::toDto);
    }

    @GetMapping(
            value = "/{accountId}/broker-reports",
            produces = MediaType.APPLICATION_NDJSON_VALUE
    )
    public Flux<BackendBrokerReport> getBrokerReport(
            @PathVariable String accountId,
            @Min(1) @RequestParam(defaultValue = "10") int pagesLimit,
            @RequestParam
            @DateTimeFormat(iso = DATE_TIME)
            OffsetDateTime from,
            @RequestParam
            @DateTimeFormat(iso = DATE_TIME)
            OffsetDateTime to
    ) {
        return GET_INVEST_API_FROM_CONTEXT
                .flatMapMany(api -> getGetBrokerReportResponseFlux(api, accountId, from, to, pagesLimit))
                .flatMapIterable(GetBrokerReportResponse::getBrokerReportList)
                .map(backendAccountMapper::toDto);
    }

    @GetMapping(
            value = "/{accountId}/foreign-dividends-reports",
            produces = MediaType.APPLICATION_NDJSON_VALUE
    )
    public Flux<BackendDividendsForeignIssuerReport> getDividendsForeignIssuerReport(
            @PathVariable String accountId,
            @Min(1) @RequestParam(defaultValue = "10") int pagesLimit,
            @RequestParam
            @DateTimeFormat(iso = DATE_TIME)
            OffsetDateTime from,
            @RequestParam
            @DateTimeFormat(iso = DATE_TIME)
            OffsetDateTime to
    ) {
        return GET_INVEST_API_FROM_CONTEXT
                .flatMapMany(api -> getGetDividendsForeignIssuerReportResponseFlux(
                        api,
                        accountId,
                        from,
                        to,
                        pagesLimit
                ))
                .flatMapIterable(GetDividendsForeignIssuerReportResponse::getDividendsForeignIssuerReportList)
                .map(backendAccountMapper::toDto);
    }

    @GetMapping("/{accountId}/operations/{status}")
    public Flux<BackendOperation> getExecutedOperations(
            @PathVariable String accountId,
            @PathVariable("status") String operationStatus,
            @RequestParam
            @DateTimeFormat(iso = DATE_TIME)
            OffsetDateTime from,
            @RequestParam
            @DateTimeFormat(iso = DATE_TIME)
            OffsetDateTime to,
            @RequestParam Optional<String> figi
    ) {
        final Mono<List<Operation>> operationsMono;
        switch (operationStatus) {
            case "executed":
                operationsMono = GET_INVEST_API_FROM_CONTEXT
                        .flatMap(api -> getExecutedOperations(api, accountId, from, to, figi));
                break;
            case "cancelled":
                operationsMono = GET_INVEST_API_FROM_CONTEXT
                        .flatMap(api -> getCancelledOperations(api, accountId, from, to, figi));
                break;
            default:
                throw new ValidationException("Can perform only 'executed' and 'cancelled' operations");
        }
        return operationsMono
                .flatMapIterable(identity())
                .map(backendTypesMapper::toDto);
    }

    @GetMapping("/{accountId}/withdraw-limits")
    public Mono<BackendWithdrawLimits> getWithdrawLimits(
            @PathVariable String accountId
    ) {
        return GET_INVEST_API_FROM_CONTEXT
                .flatMap(api -> toMono(api.getOperationsService().getWithdrawLimits(accountId)))
                .map(backendAccountMapper::toDto);
    }

    private Mono<List<Operation>> getExecutedOperations(
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
        );
    }

    private Mono<List<Operation>> getCancelledOperations(
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
        );
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
