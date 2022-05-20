package tinkoff.trading.bot.order;

import lombok.Value;
import tinkoff.trading.bot.utils.mappers.backend.BackendMoneyValue;
import tinkoff.trading.bot.utils.mappers.backend.BackendQuotation;

@Value
class BackendPostOrderResponse {
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
