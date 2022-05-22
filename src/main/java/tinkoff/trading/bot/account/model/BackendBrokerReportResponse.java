package tinkoff.trading.bot.account.model;

import lombok.Value;

@Value
class BackendBrokerReportResponse {
    String                         payloadCase;
    String                         taskId;
    BackendGetBrokerReportResponse getBrokerReportResponse;
}
