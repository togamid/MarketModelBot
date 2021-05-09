package frontend.commands;

import frontend.response.BasicResponse;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;



public class SellCommand extends BuySellCommand{
    private final String command = "sell";


    @Override
    public BasicResponse run(String[] args, MessageReceivedEvent event) {
        return new BasicResponse( createTransaction(args, false));
    }


    @Override
    public String getShortDesc() {
        return "Sell an amount of a product from a city. Usage: sell <city> <product> <amount>";
    }

    @Override
    public String getLongDesc() {
        return "Allows you to sell the amount specified to the specified city. Usage: sell <city> <product> <amount>";
    }

    @Override
    public String getCommand() {
        return command;
    }

}
