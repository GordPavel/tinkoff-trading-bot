package tinkoff.trading.bot.market.instrument;

import lombok.Value;

@Value
public class Brand {
    String uid;
    String name;
    String description;
    String info;
    String company;
    String sector;
    String countryOfRisk;
    String countryOfRiskName;
}
