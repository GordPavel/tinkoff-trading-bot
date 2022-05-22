package tinkoff.trading.bot.account.model;

import lombok.Value;
import tinkoff.trading.bot.backend.api.model.BackendQuotation;

import java.time.LocalDateTime;

@Value
public class BackendDividendsForeignIssuerReport {
    LocalDateTime    recordDate;
    LocalDateTime    paymentDate;
    String           securityName;
    String           isin;
    String           issuerCountry;
    String           currency;
    long             quantity;
    BackendQuotation dividend;
    BackendQuotation externalCommission;
    BackendQuotation dividendGross;
    BackendQuotation tax;
    BackendQuotation dividendAmount;
}
