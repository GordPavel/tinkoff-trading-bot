package tinkoff.trading.bot.account;

import lombok.RequiredArgsConstructor;
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
import tinkoff.trading.bot.account.client.BackendSandboxAccountClient;
import tinkoff.trading.bot.account.model.BackendPortfolioResponse;
import tinkoff.trading.bot.account.model.BackendPositionsResponse;
import tinkoff.trading.bot.backend.api.model.BackendAccount;
import tinkoff.trading.bot.backend.api.model.BackendMoneyValue;
import tinkoff.trading.bot.backend.api.model.BackendOperation;

import java.time.OffsetDateTime;

import static tinkoff.trading.bot.backend.api.SaveInvestApiToReactorContextConfiguration.GET_INVEST_API_FROM_CONTEXT;

@RestController
@RequestMapping("/backend/sandbox/accounts")
@RequiredArgsConstructor
public class SandboxAccountController {

    private final BackendSandboxAccountClient sandboxAccountClient;

    @PostMapping
    public Mono<String> createAccount(

    ) {
        return GET_INVEST_API_FROM_CONTEXT
                .flatMap(sandboxAccountClient::createAccount);
    }

    @DeleteMapping("/{accountId}")
    public Mono<Void> deleteAccount(
            @PathVariable String accountId
    ) {
        return GET_INVEST_API_FROM_CONTEXT
                .flatMap(api -> sandboxAccountClient.deleteAccount(api, accountId));
    }

    @GetMapping
    public Flux<BackendAccount> getAccountsList(

    ) {
        return GET_INVEST_API_FROM_CONTEXT
                .flatMapMany(sandboxAccountClient::getAccountsList);
    }

    @PostMapping("/{accountId}/pay-in")
    public Mono<BackendMoneyValue> payIn(
            @PathVariable String accountId,
            @RequestBody BackendMoneyValue amount
    ) {
        return GET_INVEST_API_FROM_CONTEXT
                .flatMap(api -> sandboxAccountClient.payIn(api, accountId, amount));
    }

    @GetMapping("/{accountId}/portfolio")
    public Mono<BackendPortfolioResponse> getPortfolio(
            @PathVariable String accountId
    ) {
        return GET_INVEST_API_FROM_CONTEXT
                .flatMap(api -> sandboxAccountClient.getPortfolio(api, accountId));
    }

    @GetMapping("/{accountId}/positions")
    public Mono<BackendPositionsResponse> getPositions(
            @PathVariable String accountId
    ) {
        return GET_INVEST_API_FROM_CONTEXT
                .flatMap(api -> sandboxAccountClient.getPositions(api, accountId));
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
                .flatMapMany(api -> sandboxAccountClient.getOperations(
                        api,
                        accountId,
                        from,
                        to,
                        operationState,
                        figi
                ));
    }

}
