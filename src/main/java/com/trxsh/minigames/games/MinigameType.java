package com.trxsh.minigames.games;

public enum MinigameType {

    TAG,
    MANHUNT,
    SNIPERS,
    TNT_TAG,
    PREY,
    CRANKED,
    FREE_FOR_ALL;

    public static MinigameType get(String name) {

        for(MinigameType t : MinigameType.values()) {

            if(t.name().equalsIgnoreCase(name))
                return t;

        }

        return null;

    }

}
