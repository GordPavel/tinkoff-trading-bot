package tinkoff.trading.bot.backend.api.model;

import com.google.protobuf.ByteString;
import com.google.protobuf.ProtocolStringList;
import com.google.protobuf.Timestamp;
import org.mapstruct.Mapper;
import ru.tinkoff.piapi.contract.v1.Account;
import ru.tinkoff.piapi.contract.v1.MoneyValue;
import ru.tinkoff.piapi.contract.v1.Operation;
import ru.tinkoff.piapi.contract.v1.Order;
import ru.tinkoff.piapi.contract.v1.OrderDirection;
import ru.tinkoff.piapi.contract.v1.OrderState;
import ru.tinkoff.piapi.contract.v1.OrderType;
import ru.tinkoff.piapi.contract.v1.Quotation;
import ru.tinkoff.piapi.contract.v1.StopOrder;
import ru.tinkoff.piapi.contract.v1.StopOrderDirection;
import ru.tinkoff.piapi.contract.v1.StopOrderType;
import ru.tinkoff.piapi.contract.v1.StreamLimit;
import ru.tinkoff.piapi.contract.v1.TradingSchedule;
import ru.tinkoff.piapi.contract.v1.UnaryLimit;
import ru.tinkoff.piapi.core.models.Money;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(
        componentModel = "spring",
        uses = {
                BackendTypesMapper.class,
        }
)
public interface BackendTypesMapper {

    default List<String> toDto(ProtocolStringList list) {
        return list.asByteStringList().stream().map(ByteString::toString).collect(Collectors.toList());
    }

    BackendTradingSchedule toDto(TradingSchedule tradingSchedule);

    BackendAccount toDto(Account account);

    BackendQuotation toDto(Quotation quotation);

    Quotation fromDto(BackendQuotation quotation);

    OrderDirection fromDtoDirection(String direction);

    StopOrderDirection fromDtoStopDirection(String direction);

    OrderType fromDtoType(String type);

    StopOrderType fromDtoStopType(String type);

    default BackendMoneyValue toDto(MoneyValue moneyValue) {
        final var units = BigDecimal.valueOf(moneyValue.getUnits());
        final var power = String.valueOf(moneyValue.getNano()).length();
        final var nanos = BigDecimal.valueOf(moneyValue.getNano()).movePointLeft(power);
        return new BackendMoneyValue(moneyValue.getCurrency(), units.add(nanos));
    }

    default MoneyValue fromDto(BackendMoneyValue moneyAmount) {
        final var quotation = toDto(moneyAmount.getValue());
        return MoneyValue.newBuilder()
                         .setCurrency(moneyAmount.getCurrency())
                         .setUnits(quotation.getUnits())
                         .setNano(quotation.getNano())
                         .build();
    }

    BackendMoneyValue toDto(Money money);

    BackendOrderState toDto(OrderState orderState);

    BackendOperation toDto(Operation operation);

    BackendOrder toDto(Order order);

    default BackendQuotation toDto(BigDecimal value) {
        final BigDecimal[] parts          = value.divideAndRemainder(BigDecimal.ONE);
        BigDecimal         integerPart    = parts[0];
        BigDecimal         fractionalPart = parts[1].multiply(BigDecimal.TEN.pow(parts[1].scale()));
        return new BackendQuotation(integerPart.longValue(), fractionalPart.intValue());
    }

    default LocalDateTime toLocalTime(Timestamp timestamp) {
        return LocalDateTime.ofEpochSecond(
                timestamp.getSeconds(),
                timestamp.getNanos(),
                ZoneOffset.UTC
        );
    }

    default ZonedDateTime toZonedTime(Timestamp timestamp) {
        return toLocalTime(timestamp).atZone(ZoneId.of("UTC"));
    }

    BackendStopOrder toDto(StopOrder stopOrder);

    BackendUnaryLimit toDto(UnaryLimit unaryLimit);

    BackendStreamLimit toDto(StreamLimit streamLimit);
}
