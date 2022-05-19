package tinkoff.trading.bot.utils.mappers.backend;

import lombok.Value;

@Value
public class BackendQuotation {
    long units;
    int  nano;
}
