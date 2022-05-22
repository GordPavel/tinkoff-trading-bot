package tinkoff.trading.bot.account.model;

import lombok.Value;
import tinkoff.trading.bot.backend.api.model.BackendMoneyValue;
import tinkoff.trading.bot.backend.api.model.BackendPortfolioPosition;
import tinkoff.trading.bot.backend.api.model.BackendQuotation;

import java.util.List;

@Value
public class BackendPortfolioResponse {
    BackendMoneyValue              totalAmountShares;
    BackendMoneyValue              totalAmountBonds;
    BackendMoneyValue              totalAmountEtf;
    BackendMoneyValue              totalAmountCurrencies;
    BackendMoneyValue              totalAmountFutures;
    BackendQuotation               expectedYield;
    List<BackendPortfolioPosition> positionsList;
}
