package tinkoff.trading.bot.market;

import org.springframework.web.bind.annotation.ResponseStatus;

import static java.lang.String.format;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ResponseStatus(NOT_FOUND)
public class AssetNotFoundException
        extends IllegalArgumentException {
    public AssetNotFoundException(String uid) {
        super(format("Asset with uid %s not found", uid));
    }
}
