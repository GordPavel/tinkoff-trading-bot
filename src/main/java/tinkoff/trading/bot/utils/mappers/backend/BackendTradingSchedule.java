package tinkoff.trading.bot.utils.mappers.backend;

import lombok.Value;

import java.util.List;

@Value
public class BackendTradingSchedule {
    String                  exchange;
    List<BackendTradingDay> daysList;
}
