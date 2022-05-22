package tinkoff.trading.bot.backend.api.model.market.model;

import lombok.Data;
import ru.tinkoff.piapi.contract.v1.Quotation;
import tinkoff.trading.bot.backend.api.model.BackendOrder;

import java.util.List;

@Data
public class BackendOrderBookResponse {
    String             figi;
    List<BackendOrder> bidsList;
    List<BackendOrder> asksList;
    int                depth;
    Quotation          limitUp;
    Quotation          limitDown;
}
