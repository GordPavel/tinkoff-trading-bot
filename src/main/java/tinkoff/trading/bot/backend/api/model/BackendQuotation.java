package tinkoff.trading.bot.backend.api.model;

import lombok.Value;

@Value
public class BackendQuotation {
    long units;
    int  nano;
}
