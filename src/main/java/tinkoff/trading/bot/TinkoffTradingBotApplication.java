package tinkoff.trading.bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoReactiveAutoConfiguration;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Profiles;
import tinkoff.trading.bot.backend.api.InvestApiPool;
import tinkoff.trading.bot.market.service.MarketDataStreamsPool;
import tinkoff.trading.bot.token.InMemoryTokensStorage;
import tinkoff.trading.bot.token.TokenAccount;

import java.util.Optional;
import java.util.UUID;

@SpringBootApplication(
        exclude = {
                MongoReactiveAutoConfiguration.class,
        }
)
@EnableConfigurationProperties(
        {
                InvestApiPool.ApiPoolConfigs.class,
                MarketDataStreamsPool.MarketDataStreamsPoolConfigs.class,
        }
)
public class TinkoffTradingBotApplication {

    @Autowired
    InMemoryTokensStorage accountsStorage;

    @Value("${PREDEFINED_TOKEN}")
    Optional<String> token;

    public static void main(String[] args) {
        SpringApplication.run(TinkoffTradingBotApplication.class, args);
    }

    @EventListener(
            value = ApplicationStartedEvent.class
    )
    public void addDebugApiToken(
            ApplicationStartedEvent event
    ) {
        if (event.getApplicationContext().getEnvironment().acceptsProfiles(Profiles.of("debug"))) {
            final var id = UUID.fromString("ec02e292-fe1f-4657-8438-a9614494b50e");
            accountsStorage.accounts.put(id, new TokenAccount(id, token.get()));
        }
    }

}
