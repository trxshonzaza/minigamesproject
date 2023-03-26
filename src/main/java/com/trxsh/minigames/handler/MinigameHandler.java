package com.trxsh.minigames.handler;

import com.trxsh.minigames.games.Minigame;

import java.util.ArrayList;

public class MinigameHandler {

    public static ArrayList<Minigame> minigames = new ArrayList();
    public static Minigame current;

    public static void addMinigame(Minigame minigame) {

        minigames.add(minigame);

    }

    public static Minigame getMinigame(String name) {

        for(Minigame minigame : minigames) {

            if(minigame.type.name().equals(name))
                return minigame;

        }

        return null;
    }

    public static void startMinigame(Minigame minigame) {

        current = minigame;

        minigame.init();
        minigame.start();
    }

}
