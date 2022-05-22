package tinkoff.trading.bot.account.model;

import lombok.Value;
import tinkoff.trading.bot.backend.api.model.BackendStreamLimit;
import tinkoff.trading.bot.backend.api.model.BackendUnaryLimit;

import java.util.List;

@Value
public class BackendTariff {
    List<BackendUnaryLimit>  unaryLimitsList;
    List<BackendStreamLimit> streamLimitsList;
}
