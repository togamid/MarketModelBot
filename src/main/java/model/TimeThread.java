package model;


import frontend.Bot;


import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class TimeThread{
    private static LocalDate nextUpdate = LocalDate.now(); //TODO: Maybe update the timezone here

    public static void run() {
        if(nextUpdate.compareTo(LocalDate.now())<=0){
            Bot.model.advanceDay();
            nextUpdate = nextUpdate.plusDays(1);
        }

        Bot.pendingTransactions.entrySet().removeIf(e -> Duration.between( e.getValue().creationTime, LocalDateTime.now()).getSeconds() >60 );
    }
}
