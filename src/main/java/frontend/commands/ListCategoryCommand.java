package frontend.commands;

import frontend.response.Response;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class ListCategoryCommand extends ListCommand {
    @Override
    public Response run(String[] args, MessageReceivedEvent event) {
        return list(true, args, event);
    }


    @Override
    public String getCommand() {
        return "listCategory";
    }

    @Override
    public String getShortDesc() {
        return  "Usage: listCategory <city> <category_name> lists products in a city by their category. Partial category names are allowed.";
    }

    @Override
    public String getLongDesc() {
        return getShortDesc() + " This is long form for list cat <city> <name>";
    }

}
