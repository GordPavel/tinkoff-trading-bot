package tinkoff.trading.bot.account;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tinkoff.trading.bot.account.client.BackendOperationsClient;
import tinkoff.trading.bot.account.model.BackendDividendsForeignIssuerReport;
import tinkoff.trading.bot.account.model.BackendPortfolioResponse;
import tinkoff.trading.bot.account.model.BackendPositionsResponse;
import tinkoff.trading.bot.account.model.BackendWithdrawLimits;
import tinkoff.trading.bot.backend.api.model.BackendBrokerReport;
import tinkoff.trading.bot.backend.api.model.BackendOperation;

import javax.validation.ValidationException;
import javax.validation.constraints.Min;
import java.time.OffsetDateTime;
import java.util.Optional;

import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME;
import static tinkoff.trading.bot.backend.api.SaveInvestApiToReactorContextConfiguration.GET_INVEST_API_FROM_CONTEXT;

@RestController
@RequestMapping("/backend/accounts")
@RequiredArgsConstructor
public class OperationsController {

    private final BackendOperationsClient operationsClient;

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
                .flatMapMany(api -> operationsClient.getAllOperations(api, accountId, figi, from, to));
    }

    @GetMapping("/{accountId}/portfolio")
    public Mono<BackendPortfolioResponse> getPortfolio(
            @PathVariable String accountId
    ) {
        return GET_INVEST_API_FROM_CONTEXT
                .flatMap(api -> operationsClient.getPortfolio(api, accountId));
    }

    @GetMapping("/{accountId}/positions")
    public Mono<BackendPositionsResponse> getPositions(
            @PathVariable String accountId
    ) {
        return GET_INVEST_API_FROM_CONTEXT
                .flatMap(api -> operationsClient.getPositions(api, accountId));
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
                .flatMapMany(api -> operationsClient.getBrokerReport(api, accountId, pagesLimit, from, to));
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
                .flatMapMany(api -> operationsClient.getDividendsForeignIssuerReport(
                        api,
                        accountId,
                        pagesLimit,
                        from,
                        to
                ));
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
        final Flux<BackendOperation> operations;
        switch (operationStatus) {
            case "executed":
                operations = GET_INVEST_API_FROM_CONTEXT
                        .flatMapMany(api -> operationsClient.getExecutedOperations(api, accountId, from, to, figi));
                break;
            case "cancelled":
                operations = GET_INVEST_API_FROM_CONTEXT
                        .flatMapMany(api -> operationsClient.getCancelledOperations(api, accountId, from, to, figi));
                break;
            default:
                throw new ValidationException("Can perform only 'executed' and 'cancelled' operations");
        }
        return operations;
    }

    @GetMapping("/{accountId}/withdraw-limits")
    public Mono<BackendWithdrawLimits> getWithdrawLimits(
            @PathVariable String accountId
    ) {
        return GET_INVEST_API_FROM_CONTEXT
                .flatMap(api -> operationsClient.getWithdrawLimits(api, accountId));
    }

}
