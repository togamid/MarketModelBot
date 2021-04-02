package frontend.response;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.function.Consumer;

public class TableResponse extends Response{

    /***
     * The header of the table. Columns have the width of their header
     */
    private final String[] header;
    private final int headerLength;

    private final String[][] rows;

    private int firstRowShown;
    private int lastRowShown;



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
        this.headerLength = headerLength;
        this.header = header;
        this.rows = rows;
    }

    /***
     *
     * @param channel the channel the response is supposed to be send in
     * @param mention the mention of the recipient
     * @param callback the function which is supposed to be run after sending
     * @return true if no errors occured
     */
    @Override
    public boolean send(TextChannel channel, String mention, Consumer<Message> callback) {
        firstRowShown = 0;
        lastRowShown = 700/headerLength;
        channel.sendMessage(sanitize(getPartialTable(firstRowShown, lastRowShown))).queue(callback); //TODO: implement scrolling
        return true;
    }

    @Override
    public String getMessageString() {
        return getPartialTable(0, rows.length-1);
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
}
