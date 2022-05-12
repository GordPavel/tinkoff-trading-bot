package tinkoff.trading.bot

import org.springframework.beans.factory.annotation.Autowired
import spock.lang.Specification
import tinkoff.trading.bot.utils.MyApplicationTest

@MyApplicationTest
class TinkoffTradingBotApplicationTests extends Specification {
    @Autowired
    TinkoffTradingBotApplication application

    def "context starts"() {
        expect:
        application != null
    }
}
