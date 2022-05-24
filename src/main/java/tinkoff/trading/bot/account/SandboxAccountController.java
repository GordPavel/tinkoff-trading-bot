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
import static tinkoff.trading.bot.backend.api.SaveInvestApiToReactorContextConfiguration.GET_INVEST_API_FROM_CONTEXT;
import static tinkoff.trading.bot.utils.CompletableFutureToMonoAdapter.toMono;

@RestController
@RequestMapping("/backend/sandbox/accounts")
@RequiredArgsConstructor
public class SandboxAccountController {

    private final BackendAccountMapper backendAccountMapper;
    private final BackendTypesMapper   backendTypesMapper;

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

    @GetMapping
    public Flux<BackendAccount> getAccountsList(

    ) {
        return GET_INVEST_API_FROM_CONTEXT
                .flatMap(api -> toMono(api.getSandboxService().getAccounts()))
                .flatMapIterable(identity())
                .map(backendTypesMapper::toDto);
    }

    @PostMapping("/{accountId}/pay-in")
    public Mono<BackendMoneyValue> payIn(
            @PathVariable String accountId,
            @RequestBody BackendMoneyValue amount
    ) {
        return GET_INVEST_API_FROM_CONTEXT
                .flatMap(api -> toMono(api.getSandboxService().payIn(accountId, backendTypesMapper.fromDto(amount))))
                .map(backendTypesMapper::toDto);
    }

    @GetMapping("/{accountId}/portfolio")
    public Mono<BackendPortfolioResponse> getPortfolio(
            @PathVariable String accountId
    ) {
        return GET_INVEST_API_FROM_CONTEXT
                .flatMap(api -> toMono(api.getSandboxService().getPortfolio(accountId)))
                .map(backendAccountMapper::toDto);
    }

    @GetMapping("/{accountId}/positions")
    public Mono<BackendPositionsResponse> getPositions(
            @PathVariable String accountId
    ) {
        return GET_INVEST_API_FROM_CONTEXT
                .flatMap(api -> toMono(api.getSandboxService().getPositions(accountId)))
                .map(backendAccountMapper::toDto);
    }

    @GetMapping("/{accountId}/operations")
    public Flux<BackendOperation> getOperations(
            @PathVariable String accountId,
            @RequestParam OffsetDateTime from,
            @RequestParam OffsetDateTime to,
            @RequestParam OperationState operationState,
            @RequestParam(required = false) String figi
    ) {
        return GET_INVEST_API_FROM_CONTEXT
                .flatMap(api -> toMono(api.getSandboxService().getOperations(
                        accountId,
                        from.atZoneSameInstant(homeZoneId).toInstant(),
                        to.atZoneSameInstant(homeZoneId).toInstant(),
                        operationState,
                        figi
                )))
                .flatMapIterable(identity())
                .map(backendTypesMapper::toDto);
    }

}
