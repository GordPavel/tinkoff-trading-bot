package tinkoff.trading.bot.backend.api.model;

import lombok.Value;

import java.util.List;

@Value
public class BackendStreamLimit {
    int          limit;
    List<String> streamsList;
}
