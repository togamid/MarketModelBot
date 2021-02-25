package frontend.commands;

import frontend.Bot;
import model.Transaction;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;


public abstract class Util {
    private static Role privilegedRole;
    public static String checkPermission(MessageReceivedEvent event){
        if(!event.isFromGuild()){
            return "This command is only available on the server!";
        }
        if(privilegedRole == null){
            List<Role> roles = event.getGuild().getRolesByName(Bot.config.get("PrivilegedRole"), true);
            if(roles.isEmpty()){
                return "Error: privileged role not found!";
            }
            else {
                privilegedRole = roles.get(0);
            }
        }
        if(event.getMember().getRoles().contains(privilegedRole)){
            return null;
        }
        else {
            return "You do not have the rights to use this role!";
        }
    }

    public static void acceptTransaction(Message message, Transaction transaction){
        if(transaction != null) {
            message.addReaction("U+1F44D").queue();
            message.addReaction("U+1F44E").queue();
            Transaction trans = transaction;
            transaction = null;
            trans.message = message;
            Bot.pendingTransactions.put(message.getId(), trans);
        }
    }
}
