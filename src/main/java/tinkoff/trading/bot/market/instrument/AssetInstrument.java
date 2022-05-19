package tinkoff.trading.bot.market.instrument;

import lombok.Value;

import java.util.List;

@Value
public class AssetInstrument {
    String               uid;
    String               figi;
    String               instrumentType;
    String               ticker;
    String               classCode;
    List<InstrumentLink> linksList;
}
