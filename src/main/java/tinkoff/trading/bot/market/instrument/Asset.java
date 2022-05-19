package tinkoff.trading.bot.market.instrument;

import lombok.Value;

import java.util.List;

@Value
public class Asset {
    String                uid;
    String                type;
    String                name;
    List<AssetInstrument> instrumentsList;
}
