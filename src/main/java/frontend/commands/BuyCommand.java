package frontend.commands;

import frontend.Bot;
import model.CityMarket;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;



public class BuyCommand implements ICommand {
    private final String command = "buy";



    @Override
    public String run(String args, MessageReceivedEvent event) {
        String[] splittedArgs = args.split(" ");
        int amount = 0;
        if(args.length() != 3){
            return "Not the right amount of args. Usage: buy <city> <product> <amount>";
        }
        try{
            amount = Integer.parseInt(splittedArgs[2]);
        } catch (NumberFormatException e){
            return "The amount has to be an integer number";
        }
        //TODO: determine Price and output it
        return null;
    }

    @Override
    public String getShortDesc() {
        return "Buy an amount of a product from a city. Usage: buy <city> <product> <amount>";
    }

    @Override
    public String getLongDesc() {
        return null;
    }

    @Override
    public String getCommand() {
        return command;
    }

    @Override
    public void init() {

    }
}
