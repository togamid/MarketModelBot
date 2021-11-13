package frontend.commands;

import frontend.Bot;
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

    public static String concat(String[] array, int beginIndex, String separator){
        return concat(array, beginIndex, array.length -1, separator);
    }
    public static String concat(String[] array, int beginIndex, int endIndex, String separator){
        if(endIndex >= array.length){
            endIndex = array.length -1;
        }
        if(beginIndex > endIndex)
            return "";
        StringBuilder builder = new StringBuilder(array[beginIndex]);
        for(int i = beginIndex+1; i<=endIndex; i++){
            builder.append(separator);
            builder.append(array[i]);
        }
        return builder.toString();
    }
}
