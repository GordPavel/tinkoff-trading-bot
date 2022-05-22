package tinkoff.trading.bot.backend.api.model;

import lombok.Value;

import java.time.LocalDateTime;

@Value
public class BackendTradingDay {
    LocalDateTime date;
    boolean       isTradingDay;
    LocalDateTime startTime;
    LocalDateTime endTime;
    LocalDateTime openingAuctionStartTime;
    LocalDateTime closingAuctionEndTime;
    LocalDateTime eveningOpeningAuctionStartTime;
    LocalDateTime eveningStartTime;
    LocalDateTime eveningEndTime;
    LocalDateTime clearingStartTime;
    LocalDateTime clearingEndTime;
    LocalDateTime premarketStartTime;
    LocalDateTime premarketEndTime;
}
