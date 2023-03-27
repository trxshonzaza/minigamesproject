package com.trxsh.minigames.listener;

import com.trxsh.minigames.games.Minigame;
import com.trxsh.minigames.games.MinigameFreezeTag;
import com.trxsh.minigames.games.MinigameType;
import com.trxsh.minigames.handler.MinigameHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMoveListener implements Listener {

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {

        if(MinigameHandler.current != null) {

            Minigame m = MinigameHandler.current;

            if(m.type == MinigameType.FREEZE_TAG) {

                for(Player player : ((MinigameFreezeTag)m).frozen) {

                    if(player.getUniqueId() == e.getPlayer().getUniqueId()) {

                        e.setCancelled(true);

                    }

                }

            }

        }

    }

}
