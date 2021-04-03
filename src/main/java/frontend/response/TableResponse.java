package frontend.response;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class TableResponse extends Response{

    public static final ConcurrentHashMap<String, TableResponse> pendingTables = new ConcurrentHashMap<>();

    /***
     * The header of the table. Columns have the width of their header
     */
    private final String[] header;

    private final String[][] rows;
    private final int rowsPerMessage;

    private int firstRowShown;
    private int lastRowShown;

    private Message message;
    private LocalDateTime lastInteracted;

    public static final String forwardReaction ="U+2B07";
    public static final String backwardReaction ="U+2B06";



    public TableResponse(String[] header, String[][] rows){
        int cols = header.length;
        for(String[] row : rows){
            if(row.length != cols){
                System.out.println("[Error] [TableResponse] All rows must have the same amount of columns as the header!");
            }
        }
        int headerLength = 0;
        for(String col : header){
            headerLength += col.length();
        }
        this.rowsPerMessage = 1800/headerLength;
        this.header = header;
        this.rows = rows;
    }

    /***
     *
     * @param channel the channel the response is supposed to be send in
     * @param mention the mention of the recipient
     * @param callbackFunction the function which is supposed to be run after sending
     * @return true if no errors occured
     */
    @Override
    public boolean send(TextChannel channel, String mention, Consumer<Message> callbackFunction) {
        firstRowShown = 0;
        lastRowShown = rowsPerMessage-1;
        lastInteracted = LocalDateTime.now();
        channel.sendMessage(sanitize(getPartialTable(firstRowShown, lastRowShown))).queue(m -> callback(m, callbackFunction)); //TODO: implement scrolling
        return true;
    }

    public void scrollForward(){
        message.addReaction(backwardReaction).queue(); //TODO: fix a bug where the scroll format emote sometimes isn't removed
        firstRowShown = lastRowShown + 1;
        lastRowShown = lastRowShown + rowsPerMessage;

        if(lastRowShown>=rows.length &&
                message.getReactions().stream().anyMatch(reaction -> reaction.getReactionEmote().getAsCodepoints().equalsIgnoreCase(forwardReaction))) {
            message.removeReaction(forwardReaction).queue();
        }
        message.editMessage(getPartialTable(firstRowShown, lastRowShown)).queue(m -> this.message = m);
        lastInteracted = LocalDateTime.now();
    }

    public void scrollBackward(){
        message.addReaction(forwardReaction).queue();
        if(firstRowShown> rowsPerMessage){
            firstRowShown -= rowsPerMessage;
            lastRowShown -= rowsPerMessage;
        }
        else {
            firstRowShown = 0;
            lastRowShown = rowsPerMessage-1;
            message.removeReaction(backwardReaction).queue();
        }
        message.editMessage(getPartialTable(firstRowShown, lastRowShown)).queue(m -> this.message = m);
        lastInteracted = LocalDateTime.now();
    }



    @Override
    public String getMessageString() {
        return getPartialTable(0, rows.length-1);
    }

    public LocalDateTime getLastInteracted(){
        return lastInteracted;
    }

    private String sanitize(String text){
        return text.replace("@", "(at)");
    }

    /***
     * Creates a partial table beginning at row start and ending at row end
     * @param start the first row included
     * @param end the last row included
     * @return a string containing the header and the specified rows, formated as a table
     */
    private String getPartialTable(int start, int end){
        StringBuilder builder = new StringBuilder("```");
        for(String col : this.header){
            builder.append(col);
        }
        for(int i = start; i<=end && i<rows.length; i++){
            builder.append("\n");
            for(int j = 0; j < header.length; j++){
                builder.append(String.format("%1$" + header[j].length() + "s", rows[i][j]));
            }
        }
        builder.append("```");
        return builder.toString();
    }

    private void callback(Message m, Consumer<Message> callbackFunction){
        if(rows.length > lastRowShown) {
            m.addReaction("U+2B07").queue();//TODO: nur wenn nötig
        }
        callbackFunction.accept(m);
        this.lastInteracted = LocalDateTime.now();
        this.message = m;
        pendingTables.put(m.getId(), this);
    }
}
