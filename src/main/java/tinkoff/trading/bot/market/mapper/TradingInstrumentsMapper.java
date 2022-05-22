package tinkoff.trading.bot.market.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import tinkoff.trading.bot.backend.api.model.BackendTypesMapper;
import tinkoff.trading.bot.market.instrument.Asset;
import tinkoff.trading.bot.market.instrument.AssetFull;
import tinkoff.trading.bot.market.instrument.Bond;
import tinkoff.trading.bot.market.instrument.Currency;
import tinkoff.trading.bot.market.instrument.Etf;
import tinkoff.trading.bot.market.instrument.Future;
import tinkoff.trading.bot.market.instrument.Share;
import tinkoff.trading.bot.market.instrument.TradingInstrument;

import static org.mapstruct.NullValueMappingStrategy.RETURN_DEFAULT;

@Mapper(
        uses = {
                BackendTypesMapper.class,
        },
        componentModel = "spring",
        nullValueMappingStrategy = RETURN_DEFAULT
)
public interface TradingInstrumentsMapper {
    @Mapping(target = "currency", source = "currency.baseCurrency")
    AssetFull toDto(ru.tinkoff.piapi.contract.v1.AssetFull assetFull);

    @Mapping(target = "instrumentType", constant = "INSTRUMENT")
    TradingInstrument toDto(ru.tinkoff.piapi.contract.v1.Instrument asset);

    Asset toDto(ru.tinkoff.piapi.contract.v1.Asset asset);

    @Mapping(target = "instrumentType", constant = "SHARE")
    Share toDto(ru.tinkoff.piapi.contract.v1.Share share);

    @Mapping(target = "instrumentType", constant = "BOND")
    Bond toDto(ru.tinkoff.piapi.contract.v1.Bond share);

    @Mapping(target = "instrumentType", constant = "CURRENCY")
    Currency toDto(ru.tinkoff.piapi.contract.v1.Currency currency);

    @Mapping(target = "instrumentType", constant = "ETF")
    Etf toDto(ru.tinkoff.piapi.contract.v1.Etf etf);

    @Mapping(target = "instrumentType", constant = "FUTURE")
    Future toDto(ru.tinkoff.piapi.contract.v1.Future future);
}
