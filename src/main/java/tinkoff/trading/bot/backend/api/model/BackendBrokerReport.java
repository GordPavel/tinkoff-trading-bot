package tinkoff.trading.bot.backend.api.model;

import lombok.Value;

import java.time.LocalDateTime;

@Value
public class BackendBrokerReport {
    String            tradeId;
    String            orderId;
    String            figi;
    String            executeSign;
    String            exchange;
    String            classCode;
    String            direction;
    String            name;
    String            ticker;
    String            party;
    String            brokerStatus;
    String            separateAgreementType;
    String            separateAgreementNumber;
    String            deliveryType;
    String            separateAgreementDate;
    LocalDateTime     clearValueDate;
    LocalDateTime     secValueDate;
    LocalDateTime     tradeDatetime;
    long              quantity;
    BackendMoneyValue price;
    BackendMoneyValue priceOrBuilder;
    BackendMoneyValue orderAmount;
    BackendQuotation  aciValue;
    BackendMoneyValue totalOrderAmount;
    BackendMoneyValue brokerCommission;
    BackendMoneyValue exchangeCommission;
    BackendMoneyValue exchangeClearingCommission;
    BackendQuotation  repoRate;
}
