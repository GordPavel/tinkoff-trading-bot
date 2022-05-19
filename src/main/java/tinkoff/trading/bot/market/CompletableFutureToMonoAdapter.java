package tinkoff.trading.bot.market;

import lombok.experimental.UtilityClass;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;

@UtilityClass
public class CompletableFutureToMonoAdapter {
    public static <T> Mono<T> toMono(CompletableFuture<T> future) {
        return Mono.fromFuture(future).doOnCancel(() -> future.cancel(true));
    }
}
