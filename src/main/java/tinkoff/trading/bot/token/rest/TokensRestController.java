package tinkoff.trading.bot.token.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import tinkoff.trading.bot.token.TokensStorage;

import java.util.UUID;

@RestController
@RequestMapping("/account")
@Validated
@RequiredArgsConstructor
class TokensRestController {

    private final TokensStorage tokensStorage;

    @PostMapping
    Mono<String> saveAccount(
            @RequestBody TokenDto tokenDto
    ) {
        return tokensStorage.saveAccount(tokenDto.getToken()).map(UUID::toString);
    }

    @DeleteMapping("/{accountId}")
    Mono<Void> deleteAccount(
            @PathVariable String accountId
    ) {
        return tokensStorage.deleteAccount(UUID.fromString(accountId));
    }

}