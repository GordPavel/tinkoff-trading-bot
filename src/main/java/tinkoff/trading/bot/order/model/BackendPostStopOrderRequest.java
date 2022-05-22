package tinkoff.trading.bot.order.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "stopType"
)
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = BackendPostStopOrderRequestTillCancel.class, name = "TillCancel"),
                @JsonSubTypes.Type(value = BackendPostStopOrderRequestTillDate.class, name = "TillDate"),
        }
)
public interface BackendPostStopOrderRequest {
    StopType getStopType();

    enum StopType {
        TILL_CANCEL,
        TILL_DATE,
        /**/
    }
}
