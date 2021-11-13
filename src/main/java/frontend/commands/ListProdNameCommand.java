package frontend.commands;

import frontend.response.Response;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class ListProdNameCommand extends ListCommand implements ICommand{
    @Override
    public Response run(String[] args, MessageReceivedEvent event) {
        return list(false, args, event);
    }

    @Override
    public String getCommand() {
        return "listProdName";
    }

    @Override
    public String getShortDesc() {
        return  "Usage: !listProdName <city> <product_name> lists products in a city. Partial names are allowed.";
    }

    @Override
    public String getLongDesc() {
        return getShortDesc() + " This is long form for !list prod <city> <name>";
    }


}
