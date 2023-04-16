package com.trxsh.minigames.games;

import com.trxsh.minigames.Main;
import com.trxsh.minigames.handler.MinigameHandler;
import com.trxsh.minigames.utility.Team;
import com.trxsh.minigames.utility.TeamType;
import org.bukkit.*;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MinigameManhunt extends Minigame {

    public List<Player> it = new ArrayList();

    private Team red;
    private Team blue;

    public MinigameManhunt(String name, String description, long duration, GameMode mode, MinigameType type) {
        super(name, description, duration, mode, type);
    }

    @Override
    public void handleAttack(Player attacker, Player hit, EntityDamageByEntityEvent e) {

        if(started) {

            for(Player player : it)
                if(hit == player)
                    return;

            blue.removePlayer(hit);
            red.addPlayer(hit);

            it.add(hit);

            if(blue.players.isEmpty()) {

                stop();
                return;

            }

            hit.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 100, 1));

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

            Bukkit.broadcastMessage(ChatColor.RED + hit.getName() + ChatColor.WHITE + " was" + ChatColor.RED + "" + ChatColor.BOLD + " TAGGED!");

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

        List<Player> determine = playing;

         Player toBeIt = ((Player)determine.toArray()[new Random().nextInt(determine.size())]);

        List<Player> teamDetermine = new ArrayList();
        List<Player> teamDetermine1 = playing;

        teamDetermine.add(toBeIt);
        teamDetermine1.remove(toBeIt);

        it.add(toBeIt);

        try {

            red = new Team(TeamType.RED, (ArrayList<Player>) teamDetermine);
            blue = new Team(TeamType.BLUE, (ArrayList<Player>) teamDetermine1);

        } catch (IllegalAccessException e) { e.printStackTrace(); }

        Bukkit.broadcastMessage(ChatColor.RED + toBeIt.getName() + ChatColor.WHITE + " is" + ChatColor.RED + "" + ChatColor.BOLD + " IT!");
        Bukkit.broadcastMessage(ChatColor.RED + "Start running!");

        started = true;

    }

    @Override
    public void stop() {

        super.teleportAllPlayersToWorldSpawn();

        Bukkit.broadcastMessage("game ended");

        for(Player player : playing) {

            player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
            player.removePotionEffect(PotionEffectType.HEAL);

        }

        playing.clear();
        super.removeAllSpectators();
        it.clear();

        red.removeAllPlayers();
        blue.removeAllPlayers();

        started = false;
        MinigameHandler.current = null;
    }
}
