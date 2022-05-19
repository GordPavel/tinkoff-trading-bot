package tinkoff.trading.bot.token;

import lombok.Value;

import java.util.UUID;

@Value
public class TokenAccount {
    UUID   id;
    String token;
}
