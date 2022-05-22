package tinkoff.trading.bot.account.model;

import lombok.Value;
import tinkoff.trading.bot.backend.api.model.BackendMoneyValue;
import tinkoff.trading.bot.backend.api.model.BackendQuotation;

@Value
public class BackendMarginAttributes {
    BackendMoneyValue liquidPortfolio;
    BackendMoneyValue startingMargin;
    BackendMoneyValue minimalMargin;
    BackendQuotation  fundsSufficiencyLevel;
    BackendMoneyValue amountOfMissingFunds;
}
