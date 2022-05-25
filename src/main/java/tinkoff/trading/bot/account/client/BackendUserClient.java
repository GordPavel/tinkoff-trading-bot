package tinkoff.trading.bot.account.client;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.tinkoff.piapi.core.InvestApi;
import tinkoff.trading.bot.account.model.BackendInfo;
import tinkoff.trading.bot.account.model.BackendMarginAttributes;
import tinkoff.trading.bot.account.model.BackendTariff;
import tinkoff.trading.bot.backend.api.model.BackendAccount;

public interface BackendUserClient {
    Mono<? extends BackendInfo> getInfo(InvestApi api);

    Mono<? extends BackendTariff> getTariff(InvestApi api);

    Flux<? extends BackendAccount> getAllAccounts(InvestApi api);

    Mono<? extends BackendMarginAttributes> getAccountMarginAttribute(InvestApi api, String accountId);
}
