package tinkoff.trading.bot.account;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tinkoff.trading.bot.account.model.BackendAccountMapper;
import tinkoff.trading.bot.account.model.BackendInfo;
import tinkoff.trading.bot.account.model.BackendMarginAttributes;
import tinkoff.trading.bot.account.model.BackendTariff;
import tinkoff.trading.bot.backend.api.model.BackendAccount;
import tinkoff.trading.bot.backend.api.model.BackendTypesMapper;

import static java.util.function.Function.identity;
import static tinkoff.trading.bot.backend.api.SaveInvestApiToReactorContextConfiguration.GET_INVEST_API_FROM_CONTEXT;
import static tinkoff.trading.bot.utils.CompletableFutureToMonoAdapter.toMono;

@RestController
@RequestMapping("/backend/user")
@RequiredArgsConstructor
public class UserController {
    private final BackendAccountMapper backendAccountMapper;
    private final BackendTypesMapper   backendTypesMapper;

    @GetMapping("/info")
    public Mono<BackendInfo> getInfo() {
        return GET_INVEST_API_FROM_CONTEXT
                .flatMap(api -> toMono(api.getUserService().getInfo()))
                .map(backendAccountMapper::toDto);
    }

    @GetMapping("/tariff")
    public Mono<BackendTariff> getTariff() {
        return GET_INVEST_API_FROM_CONTEXT
                .flatMap(api -> toMono(api.getUserService().getUserTariff()))
                .map(backendAccountMapper::toDto);
    }

    @GetMapping("/account/all")
    public Flux<BackendAccount> getAllAccounts() {
        return GET_INVEST_API_FROM_CONTEXT
                .flatMap(api -> toMono(api.getUserService().getAccounts()))
                .flatMapIterable(identity())
                .map(backendTypesMapper::toDto);
    }

    @GetMapping("/account/{accountId}/margin-attributes")
    public Mono<BackendMarginAttributes> getAccountMarginAttribute(
            @PathVariable String accountId
    ) {
        return GET_INVEST_API_FROM_CONTEXT
                .flatMap(api -> toMono(api.getUserService().getMarginAttributes(accountId)))
                .map(backendAccountMapper::toDto);
    }
}
