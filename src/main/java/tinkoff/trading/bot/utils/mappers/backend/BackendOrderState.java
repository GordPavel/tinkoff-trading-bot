package tinkoff.trading.bot.utils.mappers.backend;

import lombok.Value;

import java.time.LocalDateTime;
import java.util.List;

@Value
public class BackendOrderState {
    String                  orderId;
    String                  figi;
    String                  executionReportStatus;
    long                    lotsRequested;
    long                    lotsExecuted;
    BackendMoneyValue       initialOrderPrice;
    BackendMoneyValue       executedOrderPrice;
    BackendMoneyValue       totalOrderAmount;
    BackendMoneyValue       averagePositionPrice;
    BackendMoneyValue       initialCommission;
    BackendMoneyValue       executedCommission;
    String                  direction;
    BackendMoneyValue       initialSecurityPrice;
    List<BackendOrderStage> stagesList;
    BackendMoneyValue       serviceCommission;
    String                  currency;
    String                  orderType;
    LocalDateTime           orderDate;
}
