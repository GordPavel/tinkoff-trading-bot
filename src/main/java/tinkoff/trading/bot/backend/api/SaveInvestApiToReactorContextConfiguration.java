package tinkoff.trading.bot.backend.api;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import ru.tinkoff.piapi.core.InvestApi;
import tinkoff.trading.bot.token.TokensStorage;
import tinkoff.trading.bot.token.exception.AccountNotFoundException;
import tinkoff.trading.bot.token.exception.EmptyTokenIdHeaderException;

import java.util.UUID;

import static org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type.REACTIVE;

@Configuration
@ConditionalOnWebApplication(type = REACTIVE)
@RequiredArgsConstructor
public class SaveInvestApiToReactorContextConfiguration
        implements WebFilter {

    public static final String INVEST_API_CONTEXT_KEY = "invest.api.context.key";
    public static final String TOKEN_ID_HEADER        = "token-id";

    public final static Mono<InvestApi>
            GET_INVEST_API_FROM_CONTEXT = Mono
            .deferContextual(ctx -> Mono.justOrEmpty(ctx.<InvestApi>getOrEmpty(INVEST_API_CONTEXT_KEY))
                                        .switchIfEmpty(Mono.error(EmptyTokenIdHeaderException::new))
            );

    private final InvestApiPool apiPool;
    private final TokensStorage accountsStorage;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        final var tokenId = exchange.getRequest().getHeaders().getFirst(TOKEN_ID_HEADER);
        if (tokenId == null) {
            return chain.filter(exchange);
        }
        final var tokenUuid = UUID.fromString(tokenId);
        return accountsStorage.getAccount(tokenUuid)
                              .switchIfEmpty(Mono.error(() -> new AccountNotFoundException(tokenUuid)))
                              .map(apiPool::getTinkoffApi)
                              .flatMap(api -> chain.filter(exchange)
                                                   .contextWrite(ctx -> ctx.put(INVEST_API_CONTEXT_KEY, api))
                              );
    }
}
