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

public class MinigameFreezeTag extends Minigame {

    public List<Player> frozen = new ArrayList();
    public Player whosIt;

    private Team red;
    private Team blue;

    public MinigameFreezeTag(String name, String description, long duration, GameMode mode, MinigameType type) {
        super(name, description, duration, mode, type);
    }

    @Override
    public void handleAttack(Player attacker, Player hit, EntityDamageByEntityEvent e) {

        if(started) {

            for(Player player : frozen) {

                if(hit == player && attacker != whosIt) {

                    frozen.remove(player);

                    red.removePlayer(hit);
                    blue.addPlayer(hit);

                    removePlayerIce(hit);

                    Firework firework = hit.getWorld().spawn(hit.getLocation(), Firework.class);

                    FireworkMeta m = firework.getFireworkMeta();

                    m.addEffect(FireworkEffect.builder()

                            .flicker(true)
                            .trail(true)
                            .with(FireworkEffect.Type.BALL_LARGE)
                            .with(FireworkEffect.Type.BALL)
                            .withColor(Color.BLUE)
                            .withColor(Color.AQUA)
                            .build());

                    m.setPower(0);
                    firework.setFireworkMeta(m);

                    Bukkit.broadcastMessage(ChatColor.AQUA + hit.getName() + ChatColor.WHITE + " is" + ChatColor.AQUA + "" + ChatColor.BOLD + " UNFROZEN!");

                    return;

                }

            }

            if(frozen.contains(hit))
                return;

            if(attacker != whosIt)
                return;

            blue.removePlayer(hit);
            red.addPlayer(hit);

            frozen.add(hit);
            givePlayerIce(hit);

            if(blue.players.isEmpty()) {

                stop();
                return;

            }

            //hit.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 100, 1));

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

            Bukkit.broadcastMessage(ChatColor.RED + hit.getName() + ChatColor.WHITE + " is" + ChatColor.AQUA + "" + ChatColor.BOLD + " FROZEN!");

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

        List<Player> determine = playing;

         Player toBeIt = ((Player)determine.toArray()[new Random().nextInt(determine.size())]);

        List<Player> teamDetermine = new ArrayList();
        List<Player> teamDetermine1 = playing;

        teamDetermine.add(toBeIt);
        teamDetermine1.remove(toBeIt);

        whosIt = toBeIt;

        try {

            red = new Team(TeamType.RED, (ArrayList<Player>) teamDetermine);
            blue = new Team(TeamType.BLUE, (ArrayList<Player>) teamDetermine1);

        } catch (IllegalAccessException e) { e.printStackTrace(); }

        Bukkit.broadcastMessage(ChatColor.RED + toBeIt.getName() + ChatColor.WHITE + " is" + ChatColor.AQUA + "" + ChatColor.BOLD + " IT!");
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

        super.teleportAllPlayersToWorldSpawn();

        Bukkit.broadcastMessage("game ended");

        for(Player player : playing) {

            player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
            player.removePotionEffect(PotionEffectType.HEAL);

        }

        removeAllPlayerIce();

        playing.clear();
        frozen.clear();
        super.removeAllSpectators();

        whosIt = null;

        red.removeAllPlayers();
        blue.removeAllPlayers();

        started = false;
        MinigameHandler.current = null;
    }

    public void givePlayerIce(Player player) {

        ItemStack item = new ItemStack(Material.ICE);

        ItemMeta m = item.getItemMeta();

        m.addEnchant(Enchantment.BINDING_CURSE, 1, true);
        m.addEnchant(Enchantment.VANISHING_CURSE, 1, true);

        item.setItemMeta(m);

        player.getInventory().setHelmet(item);

    }

    public void removePlayerIce(Player player) {

        player.getInventory().setHelmet(null);

    }

    public void removeAllPlayerIce() {

        for(Player player : frozen) {

            player.getInventory().setHelmet(null);

        }

    }

}
