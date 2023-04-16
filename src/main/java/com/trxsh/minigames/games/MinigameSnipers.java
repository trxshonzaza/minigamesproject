package com.trxsh.minigames.games;

import com.trxsh.minigames.Main;
import com.trxsh.minigames.handler.MinigameHandler;
import com.trxsh.minigames.utility.ItemUtility;
import com.trxsh.minigames.utility.Team;
import com.trxsh.minigames.utility.TeamType;
import org.bukkit.*;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class MinigameSnipers extends Minigame {

    HashMap<Player, Integer> hitCount = new HashMap<>();

    public MinigameSnipers(String name, String description, long duration, GameMode mode, MinigameType type, HashMap<ItemStack, EquipmentSlot> itemsNeeded) {
        super(name, description, duration, mode, type, itemsNeeded);
    }

    @Override
    public void handleAttack(Player attacker, Player hit, EntityDamageByEntityEvent e) {

        if(started) {

            if(e.getCause() == EntityDamageEvent.DamageCause.PROJECTILE) {

                for(PotionEffect effect : hit.getActivePotionEffects())
                    if(effect.getType() == PotionEffectType.BLINDNESS) {

                        attacker.playSound(hit.getLocation(), Sound.ENTITY_ENDER_PEARL_THROW, 1, .5f);
                        attacker.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "You cannot hit a player that was recently tagged!");

                        e.setCancelled(true);

                        return;

                    }

                if(attacker.getUniqueId().equals(hit.getUniqueId())) {

                    attacker.playSound(hit.getLocation(), Sound.ENTITY_ENDER_PEARL_THROW, 1, .5f);
                    attacker.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "You cannot hit yourself!");

                    e.setCancelled(true);

                    return;

                }

                int hits = hitCount.get(attacker);
                hitCount.replace(attacker, hits + 1);

                hit.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 100, 1));
                hit.teleport(hit.getWorld().getSpawnLocation());

                Firework firework = hit.getWorld().spawn(hit.getLocation(), Firework.class);

                FireworkMeta m = firework.getFireworkMeta();

                m.addEffect(FireworkEffect.builder()

                        .flicker(true)
                        .trail(true)
                        .with(FireworkEffect.Type.BALL_LARGE)
                        .with(FireworkEffect.Type.BALL)
                        .withColor(Color.BLUE)
                        .withColor(Color.RED)
                        .build());

                m.setPower(0);
                firework.setFireworkMeta(m);

                Bukkit.broadcastMessage(ChatColor.RED + hit.getName() + ChatColor.WHITE + " was " + ChatColor.RED + "" + ChatColor.BOLD + "SHOT" + ChatColor.WHITE + " by " + ChatColor.RED + "" + ChatColor.BOLD + attacker.getName() + "!");

            }

        }

    }

    @Override
    public void start() {

        super.addAllOnlinePlayers();

        for(Player player : playing) {

            player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 200));
            player.addPotionEffect(new PotionEffect(PotionEffectType.HEAL, Integer.MAX_VALUE, 200));

            hitCount.put(player, 0);

        }

        if(playing.size() < 2) {

            Bukkit.broadcastMessage("too little players");
            return;

        }

        super.teleportAllPlayersToWorldSpawn();

        started = true;

        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.instance, new Runnable() {
            @Override
            public void run() {

                stop();

            }
        }, duration * 20L);

    }

    @Override
    public void stop() {

        super.teleportAllPlayersToWorldSpawn();

        Bukkit.broadcastMessage("game ended");

        for(Player player : playing) {

            player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
            player.removePotionEffect(PotionEffectType.HEAL);

        }

        hitCount.clear();
        playing.clear();
        super.removeAllSpectators();

        started = false;
        MinigameHandler.current = null;
    }
}
