package tinkoff.trading.bot.utils.mappers.backend;

import lombok.Value;
import ru.tinkoff.piapi.contract.v1.Quotation;

@Value
public class BackendPortfolioPosition {
    String            figi;
    String            instrumentType;
    BackendQuotation  quantity;
    BackendMoneyValue averagePositionPrice;
    Quotation         expectedYield;
    BackendMoneyValue currentNkd;
    BackendQuotation  averagePositionPricePt;
    BackendMoneyValue currentPrice;
    BackendMoneyValue averagePositionPriceFifo;
    Quotation         quantityLots;
}
