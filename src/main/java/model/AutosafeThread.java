package model;

import frontend.Bot;

public class AutosafeThread {

    public static void run(){
        Bot.dataConnector.safeModel(Bot.model);
    }
}
