package tinkoff.trading.bot.account.model;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.tinkoff.piapi.contract.v1.BrokerReport;
import ru.tinkoff.piapi.contract.v1.DividendsForeignIssuerReport;
import ru.tinkoff.piapi.contract.v1.GetInfoResponse;
import ru.tinkoff.piapi.contract.v1.GetMarginAttributesResponse;
import ru.tinkoff.piapi.contract.v1.GetUserTariffResponse;
import ru.tinkoff.piapi.contract.v1.PortfolioResponse;
import ru.tinkoff.piapi.contract.v1.PositionsResponse;
import ru.tinkoff.piapi.core.models.Portfolio;
import ru.tinkoff.piapi.core.models.Positions;
import ru.tinkoff.piapi.core.models.WithdrawLimits;
import tinkoff.trading.bot.backend.api.model.BackendBrokerReport;
import tinkoff.trading.bot.backend.api.model.BackendTypesMapper;

import static org.mapstruct.NullValueMappingStrategy.RETURN_DEFAULT;

@Mapper(
        uses = {
                BackendTypesMapper.class,
        },
        componentModel = "spring",
        nullValueMappingStrategy = RETURN_DEFAULT
)
public interface BackendAccountMapper {

    BackendPortfolioResponse toDto(PortfolioResponse portfolio);

    @Mapping(target = "totalAmountEtf", source = "totalAmountEtfs")
    @Mapping(target = "positionsList", source = "positions")
    BackendPortfolioResponse toDto(Portfolio portfolio);

    BackendPositionsResponse toDto(PositionsResponse positions);

    @Mapping(target = "securitiesList", source = "securities")
    @Mapping(target = "moneyList", source = "money")
    @Mapping(target = "futuresList", source = "futures")
    @Mapping(target = "blockedList", source = "blocked")
    BackendPositionsResponse toDto(Positions portfolio);

    BackendBrokerReport toDto(BrokerReport brokerReport);

    BackendDividendsForeignIssuerReport toDto(DividendsForeignIssuerReport dividendsForeignIssuerReport);

    BackendWithdrawLimits toDto(WithdrawLimits withdrawLimits);

    BackendInfo toDto(GetInfoResponse info);

    BackendTariff toDto(GetUserTariffResponse info);

    BackendMarginAttributes toDto(GetMarginAttributesResponse marginAttributes);
}
