package tinkoff.trading.bot.order.model;

import lombok.Value;
import tinkoff.trading.bot.backend.api.model.BackendQuotation;

import javax.validation.constraints.NotNull;

@Value
public class BackendPostOrderRequest {
    @NotNull
    String           figi;
    @NotNull
    long             quantity;
    @NotNull
    BackendQuotation price;
    @NotNull
    String           direction;
    @NotNull
    String           type;
    @NotNull
    String           orderId;
}
