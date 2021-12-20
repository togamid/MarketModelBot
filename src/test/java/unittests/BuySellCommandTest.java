package unittests;

import frontend.commands.BuyCommand;
import frontend.commands.BuySellCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BuySellCommandTest {

    private BuySellCommand buySellCommand;

    @BeforeEach
    void init() {
        this.buySellCommand = new BuyCommand();
    }

    @Test
    void createTransaction_tooFewArguments_buy(){

        String response = buySellCommand.createTransaction(new String[]{"test"}, true);
        assertEquals("Too few arguments. Usage: buy <city> <product> <amount>", response);

    }

    @Test
    void createTransaction_tooFewArguments_sell(){

        String response = buySellCommand.createTransaction(new String[]{"test"}, false);
        assertEquals("Too few arguments. Usage: sell <city> <product> <amount>", response);

    }
}
