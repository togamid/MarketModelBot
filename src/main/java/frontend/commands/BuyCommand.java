package frontend.commands;

import frontend.response.BasicResponse;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;



public class BuyCommand extends BuySellCommand {
    private final String command = "buy";


    @Override
    public BasicResponse run(String[] args, MessageReceivedEvent event) {
        return new BasicResponse(createTransaction(args, true));
    }

    @Override
    public String getShortDesc() {
        return "Buy an amount of a product from a city. Usage: buy <city> <product> <amount>";
    }

    @Override
    public String getLongDesc() {
        return "Allows you to buy the amount specified from the specified city. Usage: buy <city> <product> <amount>";
    }

    @Override
    public String getCommand() {
        return command;
    }

}
