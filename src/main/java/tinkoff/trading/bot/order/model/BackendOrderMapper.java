package tinkoff.trading.bot.order.model;

import org.mapstruct.Mapper;
import ru.tinkoff.piapi.contract.v1.PostOrderResponse;
import tinkoff.trading.bot.backend.api.model.BackendTypesMapper;

import static org.mapstruct.NullValueMappingStrategy.RETURN_DEFAULT;

@Mapper(
        uses = {
                BackendTypesMapper.class,
        },
        componentModel = "spring",
        nullValueMappingStrategy = RETURN_DEFAULT
)
public interface BackendOrderMapper {
    BackendPostOrderResponse toDto(PostOrderResponse postOrder);

}
