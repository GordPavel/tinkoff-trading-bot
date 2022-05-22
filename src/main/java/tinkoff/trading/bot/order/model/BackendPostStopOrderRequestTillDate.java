package tinkoff.trading.bot.order.model;

import lombok.Value;
import tinkoff.trading.bot.backend.api.model.BackendQuotation;

import javax.annotation.Nonnull;
import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;

@Value
public class BackendPostStopOrderRequestTillDate
        implements BackendPostStopOrderRequest {
    @NotNull
    String           figi;
    @NotNull
    long             quantity;
    @NotNull
    BackendQuotation price;
    @Nonnull
    BackendQuotation stopPrice;
    @NotNull
    String           direction;
    @NotNull
    String           type;
    @NotNull
    String           orderId;
    @NotNull
    StopType         stopType;
    @NotNull
    OffsetDateTime   expireDate;
}
