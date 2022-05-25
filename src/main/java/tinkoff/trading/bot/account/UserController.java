package tinkoff.trading.bot.account;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tinkoff.trading.bot.account.client.BackendUserClient;
import tinkoff.trading.bot.account.model.BackendInfo;
import tinkoff.trading.bot.account.model.BackendMarginAttributes;
import tinkoff.trading.bot.account.model.BackendTariff;
import tinkoff.trading.bot.backend.api.model.BackendAccount;

import static tinkoff.trading.bot.backend.api.SaveInvestApiToReactorContextConfiguration.GET_INVEST_API_FROM_CONTEXT;

@RestController
@RequestMapping("/backend/users")
@RequiredArgsConstructor
public class UserController {
    private final BackendUserClient backendUserClient;

    @GetMapping("/info")
    public Mono<BackendInfo> getInfo() {
        return GET_INVEST_API_FROM_CONTEXT
                .flatMap(backendUserClient::getInfo);
    }

    @GetMapping("/tariff")
    public Mono<BackendTariff> getTariff() {
        return GET_INVEST_API_FROM_CONTEXT
                .flatMap(backendUserClient::getTariff);
    }

    @GetMapping("/accounts")
    public Flux<BackendAccount> getAllAccounts() {
        return GET_INVEST_API_FROM_CONTEXT
                .flatMapMany(backendUserClient::getAllAccounts);
    }

    @GetMapping("/accounts/{accountId}/margin-attributes")
    public Mono<BackendMarginAttributes> getAccountMarginAttribute(
            @PathVariable String accountId
    ) {
        return GET_INVEST_API_FROM_CONTEXT
                .flatMap(api -> backendUserClient.getAccountMarginAttribute(api, accountId));
    }
}
