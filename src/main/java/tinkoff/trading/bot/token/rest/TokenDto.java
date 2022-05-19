package tinkoff.trading.bot.token.rest;

import lombok.Value;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Value
class TokenDto {
    @NotNull(message = "Token should not be null or empty")
    @Size(min = 80, max = 100, message = "Token should be valid length")
    String token;
}
