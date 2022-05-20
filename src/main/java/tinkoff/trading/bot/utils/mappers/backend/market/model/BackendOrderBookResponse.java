package tinkoff.trading.bot.utils.mappers.backend.market.model;

import lombok.Data;
import ru.tinkoff.piapi.contract.v1.Quotation;
import tinkoff.trading.bot.utils.mappers.backend.BackendOrder;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class BackendOrderBookResponse {
    String             figi;
    List<BackendOrder> bidsList;
    List<BackendOrder> asksList;
    LocalDateTime      time;
    int                depth;
    Quotation          limitUp;
    Quotation          limitDown;
}
