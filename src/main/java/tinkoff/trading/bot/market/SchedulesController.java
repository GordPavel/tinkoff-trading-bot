package tinkoff.trading.bot.market;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tinkoff.trading.bot.utils.mappers.backend.BackendTradingSchedule;
import tinkoff.trading.bot.utils.mappers.backend.BackendTypesMapper;

import javax.validation.ValidationException;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static java.util.function.Function.identity;
import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME;
import static tinkoff.trading.bot.backend.api.SaveInvestApiToReactorContextConfiguration.GET_INVEST_API_FROM_CONTEXT;
import static tinkoff.trading.bot.utils.CompletableFutureToMonoAdapter.toMono;

@RestController
@RequestMapping("/backend/instrument/schedule")
@RequiredArgsConstructor
@Slf4j
public class SchedulesController {

    private final BackendTypesMapper backendTypesMapper;
    @Value(("${internal.params.home.time.zone}"))
    private       ZoneId             homeZoneId;

    @GetMapping("/all")
    public Flux<BackendTradingSchedule> getAllExchangesSchedules(
            @RequestParam
            @DateTimeFormat(iso = DATE_TIME)
            ZonedDateTime from,
            @RequestParam
            @DateTimeFormat(iso = DATE_TIME)
            ZonedDateTime to
    ) {
        final var homeZoneFrom = from.withZoneSameInstant(homeZoneId);
        final var homeZoneTo   = to.withZoneSameInstant(homeZoneId);
        validateTimestamps(homeZoneFrom, homeZoneTo);
        return GET_INVEST_API_FROM_CONTEXT
                .flatMap(api -> toMono(api.getInstrumentsService().getTradingSchedules(
                        homeZoneFrom.toInstant(),
                        homeZoneTo.toInstant()
                )))
                .flatMapIterable(identity())
                .map(backendTypesMapper::toDto);
    }

    @GetMapping("/{exchangeId}")
    public Mono<BackendTradingSchedule> getExchangeSchedules(
            @PathVariable String exchangeId,
            @RequestParam
            @DateTimeFormat(iso = DATE_TIME)
            ZonedDateTime from,
            @RequestParam
            @DateTimeFormat(iso = DATE_TIME)
            ZonedDateTime to
    ) {
        final var homeZoneFrom = from.withZoneSameInstant(homeZoneId);
        final var homeZoneTo   = to.withZoneSameInstant(homeZoneId);
        validateTimestamps(homeZoneFrom, homeZoneTo);
        return GET_INVEST_API_FROM_CONTEXT
                .flatMap(api -> toMono(api.getInstrumentsService().getTradingSchedule(
                        exchangeId,
                        homeZoneFrom.toInstant(),
                        homeZoneTo.toInstant()
                )))
                .map(backendTypesMapper::toDto);
    }

    private void validateTimestamps(
            ZonedDateTime from,
            ZonedDateTime to
    ) {
        final var now               = ZonedDateTime.now(homeZoneId);
        final var fromTimeIncorrect = now.toLocalDate().atStartOfDay().isAfter(from.toLocalDateTime());
        if (fromTimeIncorrect) {
            throw new ValidationException(String.format(
                    "'From' time should not be before start of current day in %s zone",
                    homeZoneId.toString()
            ));
        }
    }
}
