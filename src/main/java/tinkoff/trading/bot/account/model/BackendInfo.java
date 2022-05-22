package tinkoff.trading.bot.account.model;

import lombok.Value;

import java.util.List;

@Value
public class BackendInfo {
    boolean      premStatus;
    boolean      qualStatus;
    List<String> qualifiedForWorkWithList;
    String       tariff;
}
