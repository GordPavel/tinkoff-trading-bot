package tinkoff.trading.bot.backend.api.model;

import lombok.Value;

@Value
public class BackendOrder {
    BackendQuotation price;
    long             quantity;
}
