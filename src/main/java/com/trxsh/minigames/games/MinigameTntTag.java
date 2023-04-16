package com.trxsh.minigames.games;

import com.trxsh.minigames.Main;
import com.trxsh.minigames.handler.MinigameHandler;
import com.trxsh.minigames.utility.Team;
import com.trxsh.minigames.utility.TeamType;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MinigameTntTag extends Minigame {

    public Player whosIt = null;
    public boolean canTag = false;

    private Team red;
    private Team blue;

    public MinigameTntTag(String name, String description, long duration, GameMode mode, MinigameType type) {
        super(name, description, duration, mode, type);
    }

    @Override
    public void handleAttack(Player attacker, Player hit, EntityDamageByEntityEvent e) {

        if(started && canTag) {

            if(attacker != whosIt)
                return;

            removePlayerTNT(whosIt);

            red.removePlayer(whosIt);
            blue.addPlayer(whosIt);

            whosIt = hit;
            givePlayerTNT(hit);

            red.addPlayer(hit);
            blue.removePlayer(hit);

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

        if(playing.size() < 2) {

            Bukkit.broadcastMessage("too little players");
            return;

        }

        super.teleportAllPlayersToWorldSpawn();

        started = true;

        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.instance, new Runnable() {
            @Override
            public void run() {

                List<Player> determine = playing;

                whosIt = ((Player)determine.toArray()[new Random().nextInt(determine.size())]);

                List<Player> teamDetermine = new ArrayList();
                List<Player> teamDetermine1 = playing;

                teamDetermine.add(whosIt);
                teamDetermine1.remove(whosIt);

                try {

                    red = new Team(TeamType.RED, (ArrayList<Player>) teamDetermine);
                    blue = new Team(TeamType.BLUE, (ArrayList<Player>) teamDetermine1);

                } catch (IllegalAccessException e) { e.printStackTrace(); }

                canTag = true;
                givePlayerTNT(whosIt);

                Bukkit.broadcastMessage(ChatColor.RED + whosIt.getName() + ChatColor.WHITE + " is " + ChatColor.RED + "" + ChatColor.BOLD + "IT!");
                Bukkit.broadcastMessage(ChatColor.RED + "Start running!");

                handleTNTWait();

            }
        }, 5L * 20L);

    }

    @Override
    public void stop() {

        teleportAllPlayersToWorldSpawn();

        Bukkit.broadcastMessage("game ended");

        for(Player player : playing) {

            player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
            player.removePotionEffect(PotionEffectType.HEAL);

        }

        for(Player player : spectating) {

            player.setGameMode(GameMode.ADVENTURE);

        }

        playing.clear();
        super.removeAllSpectators();

        whosIt = null;

        red.removeAllPlayers();
        blue.removeAllPlayers();

        started = false;
        MinigameHandler.current = null;
    }

    public void handleTNTDeath() {

        canTag = false;

        red.removePlayer(whosIt);

        whosIt.getWorld().playSound(whosIt.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1, 1);
        whosIt.getWorld().spawnParticle(Particle.EXPLOSION_HUGE, whosIt.getLocation(), 50);

        whosIt.getInventory().setHelmet(null);

        Bukkit.broadcastMessage(whosIt.getName() + " died!");

        addSpectator(whosIt);
        whosIt = null;

        if(blue.players.size() == 1) {

            stop();
            return;

        }

        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.instance, new Runnable() {
            @Override
            public void run() {

                List<Player> determine = blue.players;

                whosIt = ((Player)determine.toArray()[new Random().nextInt(determine.size())]);

                canTag = true;
                givePlayerTNT(whosIt);

                blue.removePlayer(whosIt);
                red.addPlayer(whosIt);

                Bukkit.broadcastMessage(ChatColor.RED + whosIt.getName() + ChatColor.WHITE + " is " + ChatColor.RED + "" + ChatColor.BOLD + "IT!");
                Bukkit.broadcastMessage(ChatColor.RED + "Start running!");

                handleTNTWait();

            }
        }, 5L * 20L);

    }

    public void givePlayerTNT(Player player) {

        ItemStack item = new ItemStack(Material.TNT);

        ItemMeta m = item.getItemMeta();

        m.addEnchant(Enchantment.BINDING_CURSE, 1, true);
        m.addEnchant(Enchantment.VANISHING_CURSE, 1, true);

        item.setItemMeta(m);

        player.getInventory().setHelmet(item);

    }

    public void removePlayerTNT(Player player) {

        player.getInventory().setHelmet(null);

    }

    public void handleTNTWait() {

        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.instance, new Runnable() {
            @Override
            public void run() {

                handleTNTDeath();

            }
        }, 30L * 20L);

    }

}
