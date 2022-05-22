package tinkoff.trading.bot.account.model;

import lombok.Value;
import tinkoff.trading.bot.backend.api.model.BackendMoneyValue;

import java.util.List;

@Value
public class BackendWithdrawLimits {
    List<BackendMoneyValue> money;
    List<BackendMoneyValue> blocked;
    List<BackendMoneyValue> blockedGuarantee;
}
