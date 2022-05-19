package tinkoff.trading.bot.utils.mappers.backend;

import com.google.protobuf.ByteString;
import com.google.protobuf.ProtocolStringList;
import com.google.protobuf.Timestamp;
import org.mapstruct.Mapper;
import ru.tinkoff.piapi.contract.v1.Account;
import ru.tinkoff.piapi.contract.v1.Candle;
import ru.tinkoff.piapi.contract.v1.MoneyValue;
import ru.tinkoff.piapi.contract.v1.Operation;
import ru.tinkoff.piapi.contract.v1.OrderDirection;
import ru.tinkoff.piapi.contract.v1.OrderState;
import ru.tinkoff.piapi.contract.v1.OrderType;
import ru.tinkoff.piapi.contract.v1.Quotation;
import ru.tinkoff.piapi.contract.v1.TradingSchedule;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface BackendTypesMapper {

    default List<String> toDto(ProtocolStringList list) {
        return list.asByteStringList().stream().map(ByteString::toString).collect(Collectors.toList());
    }

    BackendTradingSchedule toDto(TradingSchedule tradingSchedule);

    BackendAccount toDto(Account account);

    BackendQuotation toDto(Quotation quotation);

    Quotation fromDto(BackendQuotation quotation);

    OrderDirection fromDtoDirection(String direction);

    OrderType fromDtoType(String type);

    BackendMoneyValue toDto(MoneyValue moneyValue);

    MoneyValue fromDto(BackendMoneyValue moneyAmount);

    BackendOrderState toDto(OrderState orderState);

    BackendOperation toDto(Operation operation);

    BackendCandle toDto(Candle candle);

    default LocalDateTime toTime(Timestamp timestamp) {
        return LocalDateTime.ofEpochSecond(
                timestamp.getSeconds(),
                timestamp.getNanos(),
                ZoneOffset.UTC
        );
    }
}
