package tinkoff.trading.bot.backend.api;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.stereotype.Service;
import ru.tinkoff.piapi.core.InvestApi;
import tinkoff.trading.bot.token.TokenAccount;

import java.time.Duration;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Service
public class InvestApiPool {

    private final LoadingCache<TokenAccount, InvestApi> apiPool;

    public InvestApiPool(InvestApiFactory apiFactory, ApiPoolConfigs configs) {
        this.apiPool = CacheBuilder.newBuilder()
                                   .maximumSize(configs.getPoolMaxSize())
                                   .expireAfterWrite(configs.getExpiresAfter().toMillis(), TimeUnit.MILLISECONDS)
                                   .build(CacheLoader.from((TokenAccount account) -> apiFactory.createApi(account.getToken())));
    }

    public InvestApi getTinkoffApi(TokenAccount account) {
        try {
            return apiPool.get(account);
        } catch (ExecutionException e) {
            throw new IllegalStateException(e);
        }
    }

    @Value
    @ConfigurationProperties("tinkoff.api.pool")
    @ConstructorBinding
    public static class ApiPoolConfigs {
        int      poolMaxSize;
        Duration expiresAfter;
    }
}
