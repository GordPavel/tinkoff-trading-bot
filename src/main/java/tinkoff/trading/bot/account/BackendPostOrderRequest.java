package tinkoff.trading.bot.account;

import lombok.Value;
import tinkoff.trading.bot.utils.mappers.backend.BackendQuotation;

import javax.validation.constraints.NotNull;

@Value
class BackendPostOrderRequest {
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
