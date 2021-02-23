package model;


import frontend.Bot;


import java.time.Duration;
import java.time.LocalDateTime;

public class TimeThread{

    public static void run() {
        Bot.model.advanceDay();
        Bot.pendingTransactions.entrySet().removeIf(e -> Duration.between( e.getValue().creationTime, LocalDateTime.now()).getSeconds() >60 );
    }
}
