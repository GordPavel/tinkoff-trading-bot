package tinkoff.trading.bot.utils.mappers.backend;

import lombok.Value;

import java.time.LocalDateTime;

@Value
public class BackendAccount {
    String        id;
    String        type;
    String        name;
    String        status;
    LocalDateTime openedDate;
    LocalDateTime closedDate;
    String        accessLevel;
}
