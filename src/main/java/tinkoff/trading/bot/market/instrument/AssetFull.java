package tinkoff.trading.bot.market.instrument;

import lombok.Value;

import java.time.LocalDateTime;
import java.util.List;

@Value
public class AssetFull {
    String                uid;
    String                type;
    String                name;
    String                nameBrief;
    String                description;
    LocalDateTime         deletedAt;
    List<String>          requiredTestsList;
    String                currency;
    AssetSecurity         security;
    String                gosRegCode;
    String                cfi;
    String                codeNsd;
    String                status;
    Brand                 brand;
    LocalDateTime         updatedAt;
    String                brCode;
    String                brCodeName;
    List<AssetInstrument> instrumentsList;
}
