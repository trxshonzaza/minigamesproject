package com.trxsh.minigames.games;

import com.trxsh.minigames.Main;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.Random;
import java.util.UUID;

public class MinigameTag extends Minigame {

    public Player whosIt;

    public MinigameTag(String name, String description, long duration, GameMode mode, MinigameType type) {
        super(name, description, duration, mode, type);
    }

    @Override
    public void handleAttack(Player attacker, Player hit, EntityDamageByEntityEvent e) {

        if(started) {

            if(attacker != whosIt)
                return;

            whosIt = hit;

            whosIt.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 100, 1));

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

            Bukkit.broadcastMessage(ChatColor.RED + whosIt.getName() + ChatColor.WHITE + " is " + ChatColor.RED + "" + ChatColor.BOLD + " IT!");

        }

    }

    @Override
    public void start() {

        super.addAllOnlinePlayers();

        for(Player player : playing) {

            player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 200));
            player.addPotionEffect(new PotionEffect(PotionEffectType.HEAL, Integer.MAX_VALUE, 200));

        }

        if(playing.size() < 1) {

            Bukkit.broadcastMessage("too little players");
            return;

        }

        super.teleportAllPlayersToWorldSpawn();

        List<Player> determine = super.playing;

        whosIt = ((Player)determine.toArray()[new Random().nextInt(determine.size())]);

        Bukkit.broadcastMessage(ChatColor.RED + whosIt.getName() + ChatColor.WHITE + " is " + ChatColor.RED + "" + ChatColor.BOLD + " IT!");
        Bukkit.broadcastMessage(ChatColor.RED + "Start running!");

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

        if(whosIt != null) {

            super.teleportAllPlayersToWorldSpawn();

            Bukkit.broadcastMessage("game ended");

        }

        for(Player player : playing) {

            player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
            player.removePotionEffect(PotionEffectType.HEAL);

        }

        playing.clear();
        spectating.clear();

        started = false;
    }
}