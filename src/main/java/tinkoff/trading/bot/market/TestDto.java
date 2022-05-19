package tinkoff.trading.bot.market;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.Min;

@Data
@ToString
public class TestDto {
    @Min(2)
    private String test;
}
