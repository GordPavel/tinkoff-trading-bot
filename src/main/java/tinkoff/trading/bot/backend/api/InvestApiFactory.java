package tinkoff.trading.bot.backend.api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.tinkoff.piapi.core.InvestApi;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@Service
public class InvestApiFactory {

    @Value("spring.application.name")
    private       String                                          applicationName;
    private final Map<InvestApiType, Function<String, InvestApi>> types = new HashMap<>() {{
        put(InvestApiType.STANDARD, token -> InvestApi.create(token, applicationName));
        put(InvestApiType.READ_ONLY, token -> InvestApi.createReadonly(token, applicationName));
        put(InvestApiType.SANDBOX, token -> InvestApi.createSandbox(token, applicationName));
    }};
    @Value("${tinkoff.api.type:SANDBOX}")
    private       InvestApiType                                   createApiType;

    public InvestApi createApi(String token) {
        return createApi(token, createApiType);
    }

    private InvestApi createApi(String token, InvestApiType type) {
        return Optional.ofNullable(types.get(type))
                       .map(supplier -> supplier.apply(token))
                       .orElseThrow(IllegalArgumentException::new);
    }


    public enum InvestApiType {
        STANDARD,
        READ_ONLY,
        SANDBOX,
    }
}
