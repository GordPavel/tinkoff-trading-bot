package tinkoff.trading.bot.account;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.tinkoff.piapi.contract.v1.OperationState;
import tinkoff.trading.bot.utils.mappers.backend.BackendAccount;
import tinkoff.trading.bot.utils.mappers.backend.BackendMoneyValue;
import tinkoff.trading.bot.utils.mappers.backend.BackendOperation;
import tinkoff.trading.bot.utils.mappers.backend.BackendTypesMapper;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import static java.util.function.Function.identity;
import static tinkoff.trading.bot.backend.api.SaveInvestApiToReactorContextConfiguration.GET_INVEST_API_FROM_CONTEXT;
import static tinkoff.trading.bot.utils.CompletableFutureToMonoAdapter.toMono;

@RestController
@RequestMapping("/backend/sandbox/account")
@RequiredArgsConstructor
public class SandboxAccountController {

    private final BackendAccountMapper mapper;
    private final BackendTypesMapper   protobufMapper;

    @Value(("${internal.params.home.time.zone}"))
    private ZoneId homeZoneId;

    @PostMapping
    public Mono<String> createAccount(

    ) {
        return GET_INVEST_API_FROM_CONTEXT.flatMap(api -> toMono(api.getSandboxService().openAccount()));
    }

    @DeleteMapping("/{accountId}")
    public Mono<Void> deleteAccount(
            @PathVariable String accountId
    ) {
        return GET_INVEST_API_FROM_CONTEXT.flatMap(api -> toMono(api.getSandboxService().closeAccount(accountId)));
    }

    @GetMapping("/all")
    public Flux<BackendAccount> getAccountsList(

    ) {
        return GET_INVEST_API_FROM_CONTEXT
                .flatMap(api -> toMono(api.getSandboxService().getAccounts()))
                .flatMapIterable(identity())
                .map(protobufMapper::toDto);
    }

    @PostMapping("/{accountId}/pay-in")
    public Mono<BackendMoneyValue> payIn(
            @PathVariable String accountId,
            @RequestBody BackendMoneyValue amount
    ) {
        return GET_INVEST_API_FROM_CONTEXT
                .flatMap(api -> toMono(api.getSandboxService().payIn(accountId, protobufMapper.fromDto(amount))))
                .map(protobufMapper::toDto);
    }

    @GetMapping("/{accountId}/portfolio")
    public Mono<BackendPortfolioResponse> getPortfolio(
            @PathVariable String accountId
    ) {
        return GET_INVEST_API_FROM_CONTEXT
                .flatMap(api -> toMono(api.getSandboxService().getPortfolio(accountId)))
                .map(mapper::toDto);
    }

    @GetMapping("/{accountId}/positions")
    public Mono<BackendPositionsResponse> getPositions(
            @PathVariable String accountId
    ) {
        return GET_INVEST_API_FROM_CONTEXT
                .flatMap(api -> toMono(api.getSandboxService().getPositions(accountId)))
                .map(mapper::toDto);
    }

    @GetMapping("/{accountId}/operation/all")
    public Flux<BackendOperation> getOperations(
            @PathVariable String accountId,
            @RequestParam ZonedDateTime from,
            @RequestParam ZonedDateTime to,
            @RequestParam OperationState operationState,
            @RequestParam(required = false) String figi
    ) {
        return GET_INVEST_API_FROM_CONTEXT
                .flatMap(api -> toMono(api.getSandboxService().getOperations(
                        accountId,
                        from.withZoneSameInstant(homeZoneId).toInstant(),
                        to.withZoneSameInstant(homeZoneId).toInstant(),
                        operationState,
                        figi
                )))
                .flatMapIterable(identity())
                .map(protobufMapper::toDto);
    }

}
