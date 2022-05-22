package tinkoff.trading.bot.order;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tinkoff.trading.bot.backend.api.model.BackendOrderState;
import tinkoff.trading.bot.backend.api.model.BackendTypesMapper;
import tinkoff.trading.bot.order.model.BackendOrderMapper;
import tinkoff.trading.bot.order.model.BackendPostOrderRequest;
import tinkoff.trading.bot.order.model.BackendPostOrderResponse;

import java.time.LocalDateTime;
import java.time.ZoneId;

import static java.util.function.Function.identity;
import static tinkoff.trading.bot.backend.api.SaveInvestApiToReactorContextConfiguration.GET_INVEST_API_FROM_CONTEXT;
import static tinkoff.trading.bot.utils.CompletableFutureToMonoAdapter.toMono;

@RestController
@RequestMapping("/backend/sandbox/account/{accountId}/order")
@RequiredArgsConstructor
@ConditionalOnProperty(
        value = "tinkoff.api.type",
        havingValue = "SANDBOX"
)
public class SandboxOrderController {

    private final BackendOrderMapper backendOrderMapper;
    private final BackendTypesMapper backendTypesMapper;

    @GetMapping("/all")
    public Flux<BackendOrderState> getOrders(
            @PathVariable String accountId
    ) {
        return GET_INVEST_API_FROM_CONTEXT
                .flatMap(api -> toMono(api.getSandboxService().getOrders(accountId)))
                .flatMapIterable(identity())
                .map(backendTypesMapper::toDto);
    }

    @GetMapping("/{orderId}")
    public Flux<BackendOrderState> getOrder(
            @PathVariable String accountId,
            @PathVariable String orderId
    ) {
        return GET_INVEST_API_FROM_CONTEXT
                .flatMapMany(api -> toMono(api.getSandboxService().getOrderState(accountId, orderId)))
                .map(backendTypesMapper::toDto);
    }

    @PostMapping
    public Mono<BackendPostOrderResponse> postOrder(
            @PathVariable String accountId,
            @RequestBody BackendPostOrderRequest request
    ) {
        return GET_INVEST_API_FROM_CONTEXT
                .flatMap(api -> toMono(api.getSandboxService().postOrder(
                        request.getFigi(),
                        request.getQuantity(),
                        backendTypesMapper.fromDto(request.getPrice()),
                        backendTypesMapper.fromDtoDirection(request.getDirection()),
                        accountId,
                        backendTypesMapper.fromDtoType(request.getType()),
                        request.getOrderId()
                )))
                .map(backendOrderMapper::toDto);
    }

    @DeleteMapping("/{orderId}")
    public Mono<LocalDateTime> cancelOrder(
            @PathVariable String accountId,
            @PathVariable String orderId
    ) {
        return GET_INVEST_API_FROM_CONTEXT
                .flatMap(api -> toMono(api.getSandboxService().cancelOrder(accountId, orderId)))
                .map(cancelOrderTime -> LocalDateTime.ofInstant(cancelOrderTime, ZoneId.of("UTC")));
    }
}
