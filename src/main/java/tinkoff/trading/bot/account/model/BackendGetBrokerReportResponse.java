package tinkoff.trading.bot.account.model;

import lombok.Value;
import tinkoff.trading.bot.backend.api.model.BackendBrokerReport;

import java.util.List;

@Value
class BackendGetBrokerReportResponse {
    List<BackendBrokerReport> brokerReportList;
    int                       itemsCount;
    int                       pagesCount;
    int                       page;
}
