package tinkoff.trading.bot.account;

import org.mapstruct.Mapper;
import ru.tinkoff.piapi.contract.v1.PortfolioResponse;
import ru.tinkoff.piapi.contract.v1.PositionsResponse;
import ru.tinkoff.piapi.contract.v1.PostOrderResponse;
import tinkoff.trading.bot.utils.mappers.backend.BackendTypesMapper;

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

    BackendPositionsResponse toDto(PositionsResponse positions);

    BackendPostOrderResponse toDto(PostOrderResponse postOrder);


}
