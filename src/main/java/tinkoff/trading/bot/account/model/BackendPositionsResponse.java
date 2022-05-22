package tinkoff.trading.bot.account.model;

import lombok.Value;
import tinkoff.trading.bot.backend.api.model.BackendMoneyValue;
import tinkoff.trading.bot.backend.api.model.BackendPositionsFutures;

import java.util.List;

@Value
public class BackendPositionsResponse {
    List<BackendMoneyValue>       moneyList;
    List<BackendMoneyValue>       blockedList;
    List<BackendPositionsFutures> securitiesList;
    boolean                       limitsLoadingInProgress;
    List<BackendPositionsFutures> futuresList;
}
