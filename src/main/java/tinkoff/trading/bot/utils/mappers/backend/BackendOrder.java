package tinkoff.trading.bot.utils.mappers.backend;

import lombok.Value;

@Value
public class BackendOrder {
    BackendQuotation price;
    long             quantity;
}
