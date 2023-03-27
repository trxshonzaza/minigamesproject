package com.trxsh.minigames.listener;

import com.trxsh.minigames.games.Minigame;
import com.trxsh.minigames.handler.MinigameHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {

        if(MinigameHandler.current != null) {

            Minigame m = MinigameHandler.current;

            m.addSpectator(e.getPlayer());

            e.getPlayer().sendMessage("game is current starting, please wait until game is finished");

        }

    }

}
