package tinkoff.trading.bot.account;

import lombok.Value;
import tinkoff.trading.bot.utils.mappers.backend.BackendMoneyValue;
import tinkoff.trading.bot.utils.mappers.backend.BackendPortfolioPosition;
import tinkoff.trading.bot.utils.mappers.backend.BackendQuotation;

import java.util.List;

@Value
class BackendPortfolioResponse {
    BackendMoneyValue              totalAmountShares;
    BackendMoneyValue              totalAmountBonds;
    BackendMoneyValue              totalAmountEtf;
    BackendMoneyValue              totalAmountCurrencies;
    BackendMoneyValue              totalAmountFutures;
    BackendQuotation               expectedYield;
    List<BackendPortfolioPosition> positionsList;
}
