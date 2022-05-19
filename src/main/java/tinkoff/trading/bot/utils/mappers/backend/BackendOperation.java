package tinkoff.trading.bot.utils.mappers.backend;

import lombok.Value;

import java.time.LocalDateTime;
import java.util.List;

@Value
public class BackendOperation {
    String                      id;
    String                      parentOperationId;
    String                      currency;
    BackendMoneyValue           payment;
    BackendMoneyValue           price;
    String                      state;
    long                        quantity;
    String                      figi;
    String                      instrumentType;
    LocalDateTime               date;
    String                      type;
    String                      operationType;
    List<BackendOperationTrade> tradesList;
}
