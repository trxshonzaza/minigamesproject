package com.trxsh.minigames.listener;

import com.trxsh.minigames.games.Minigame;
import com.trxsh.minigames.handler.MinigameHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class PlayerAttackListener implements Listener {

    @EventHandler
    public void onPlayerAttack(EntityDamageByEntityEvent e) {

        if(e.getEntity() instanceof Player) {

            if(e.getDamager() instanceof Player) {

                Player damaged = (Player)e.getEntity();
                Player attacker = (Player)e.getDamager();

                Bukkit.broadcastMessage(attacker.getName() + " hit " + damaged.getName() + "!");

                if(MinigameHandler.current != null) {

                    Minigame m = MinigameHandler.current;

                    m.handleAttack(attacker, damaged, e);

                } else {

                    e.setCancelled(true);

                }

            }

        }

    }

}
