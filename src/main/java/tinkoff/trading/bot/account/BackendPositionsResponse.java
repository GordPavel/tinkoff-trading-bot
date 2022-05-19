package tinkoff.trading.bot.account;

import lombok.Value;
import tinkoff.trading.bot.utils.mappers.backend.BackendMoneyValue;
import tinkoff.trading.bot.utils.mappers.backend.BackendPositionsFutures;

import java.util.List;

@Value
class BackendPositionsResponse {
    List<BackendMoneyValue>       moneyList;
    List<BackendMoneyValue>       blockedList;
    List<BackendPositionsFutures> securitiesList;
    boolean                       limitsLoadingInProgress;
    List<BackendPositionsFutures> futuresList;
}
