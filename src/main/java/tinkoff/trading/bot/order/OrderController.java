package tinkoff.trading.bot.order;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.tinkoff.piapi.core.StopOrdersService;
import tinkoff.trading.bot.backend.api.model.BackendOrderState;
import tinkoff.trading.bot.backend.api.model.BackendStopOrder;
import tinkoff.trading.bot.backend.api.model.BackendTypesMapper;
import tinkoff.trading.bot.order.model.BackendOrderMapper;
import tinkoff.trading.bot.order.model.BackendPostOrderRequest;
import tinkoff.trading.bot.order.model.BackendPostOrderResponse;
import tinkoff.trading.bot.order.model.BackendPostStopOrderRequest;
import tinkoff.trading.bot.order.model.BackendPostStopOrderRequestTillCancel;
import tinkoff.trading.bot.order.model.BackendPostStopOrderRequestTillDate;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static java.util.function.Function.identity;
import static tinkoff.trading.bot.backend.api.SaveInvestApiToReactorContextConfiguration.GET_INVEST_API_FROM_CONTEXT;
import static tinkoff.trading.bot.utils.CompletableFutureToMonoAdapter.toMono;

@RestController
@RequestMapping("/backend/accounts/{accountId}/orders")
@RequiredArgsConstructor
@ConditionalOnExpression("${tinkoff.api.type}!='SANDBOX'")
public class OrderController {
    private final BackendOrderMapper backendOrderMapper;
    private final BackendTypesMapper backendTypesMapper;

    @Value(("${internal.params.home.time.zone}"))
    private ZoneId homeZoneId;

    @GetMapping
    public Flux<BackendOrderState> getOrders(
            @PathVariable String accountId
    ) {
        return GET_INVEST_API_FROM_CONTEXT
                .flatMap(api -> toMono(api.getOrdersService().getOrders(accountId)))
                .flatMapIterable(identity())
                .map(backendTypesMapper::toDto);
    }

    @GetMapping("/{orderId}")
    public Flux<BackendOrderState> getOrder(
            @PathVariable String accountId,
            @PathVariable String orderId
    ) {
        return GET_INVEST_API_FROM_CONTEXT
                .flatMapMany(api -> toMono(api.getOrdersService().getOrderState(accountId, orderId)))
                .map(backendTypesMapper::toDto);
    }

    @PostMapping
    public Mono<BackendPostOrderResponse> postOrder(
            @PathVariable String accountId,
            @RequestBody BackendPostOrderRequest request
    ) {
        return GET_INVEST_API_FROM_CONTEXT
                .flatMap(api -> toMono(api.getOrdersService().postOrder(
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
    public Mono<ZonedDateTime> cancelOrder(
            @PathVariable String accountId,
            @PathVariable String orderId
    ) {
        return GET_INVEST_API_FROM_CONTEXT
                .flatMap(api -> toMono(api.getOrdersService().cancelOrder(accountId, orderId)))
                .map(cancelOrderTime -> OffsetDateTime
                        .ofInstant(cancelOrderTime, ZoneId.of("UTC"))
                        .atZoneSameInstant(homeZoneId)
                );
    }

    @GetMapping("/stops")
    public Flux<BackendStopOrder> getAllStopOrders(
            @PathVariable String accountId
    ) {
        return GET_INVEST_API_FROM_CONTEXT
                .flatMap(api -> toMono(api.getStopOrdersService().getStopOrders(accountId)))
                .flatMapIterable(identity())
                .map(backendTypesMapper::toDto);
    }

    @PostMapping("/stops")
    public Mono<String> postStopOrder(
            @PathVariable String accountId,
            @RequestBody BackendPostStopOrderRequest request
    ) {
        return GET_INVEST_API_FROM_CONTEXT
                .flatMap(api -> postStopOrder(
                        api.getStopOrdersService(),
                        request,
                        accountId
                ));
    }

    @DeleteMapping("/stops/{orderId}")
    public Mono<ZonedDateTime> cancelStopOrder(
            @PathVariable String accountId,
            @PathVariable String orderId
    ) {
        return GET_INVEST_API_FROM_CONTEXT
                .flatMap(api -> toMono(api.getStopOrdersService().cancelStopOrder(accountId, orderId)))
                .map(cancelOrderTime -> OffsetDateTime
                        .ofInstant(cancelOrderTime, ZoneId.of("UTC"))
                        .atZoneSameInstant(homeZoneId)
                );
    }

    private Mono<String> postStopOrder(
            StopOrdersService stopOrdersService,
            BackendPostStopOrderRequest request,
            String accountId
    ) {
        switch (request.getStopType()) {
            case TILL_CANCEL:
                final var requestTillCancel = (BackendPostStopOrderRequestTillCancel) request;
                return toMono(stopOrdersService.postStopOrderGoodTillCancel(
                        requestTillCancel.getFigi(),
                        requestTillCancel.getQuantity(),
                        backendTypesMapper.fromDto(requestTillCancel.getPrice()),
                        backendTypesMapper.fromDto(requestTillCancel.getStopPrice()),
                        backendTypesMapper.fromDtoStopDirection(requestTillCancel.getDirection()),
                        accountId,
                        backendTypesMapper.fromDtoStopType(requestTillCancel.getType())
                ));
            case TILL_DATE:
                final var requestTillDate = (BackendPostStopOrderRequestTillDate) request;
                return toMono(stopOrdersService.postStopOrderGoodTillDate(
                        requestTillDate.getFigi(),
                        requestTillDate.getQuantity(),
                        backendTypesMapper.fromDto(requestTillDate.getPrice()),
                        backendTypesMapper.fromDto(requestTillDate.getStopPrice()),
                        backendTypesMapper.fromDtoStopDirection(requestTillDate.getDirection()),
                        accountId,
                        backendTypesMapper.fromDtoStopType(requestTillDate.getType()),
                        requestTillDate.getExpireDate().atZoneSameInstant(homeZoneId).toInstant()
                ));
            default:
                return Mono.error(new IllegalStateException());
        }
    }
}
