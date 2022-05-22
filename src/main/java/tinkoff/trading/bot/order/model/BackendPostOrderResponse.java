package tinkoff.trading.bot.order.model;

import lombok.Value;
import tinkoff.trading.bot.backend.api.model.BackendMoneyValue;
import tinkoff.trading.bot.backend.api.model.BackendQuotation;

@Value
public class BackendPostOrderResponse {
    String            orderId;
    String            executionReportStatus;
    long              lotsRequested;
    long              lotsExecuted;
    BackendMoneyValue initialOrderPrice;
    BackendMoneyValue executedOrderPrice;
    BackendMoneyValue totalOrderAmount;
    BackendMoneyValue initialCommission;
    BackendMoneyValue executedCommission;
    BackendMoneyValue aciValue;
    String            figi;
    String            direction;
    BackendMoneyValue initialSecurityPrice;
    String            orderType;
    String            message;
    BackendQuotation  initialOrderPricePt;
}
