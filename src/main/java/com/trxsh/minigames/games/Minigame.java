package com.trxsh.minigames.games;

import com.trxsh.minigames.data.GameStatistics;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class Minigame {

    public String name;
    public String description;

    public boolean started;

    public long duration;

    public ArrayList<Player> playing = new ArrayList();
    public ArrayList<Player> spectating = new ArrayList();

    public GameStatistics stats;

    public GameMode mode;

    public MinigameType type;

    public HashMap<ItemStack, EquipmentSlot> itemsNeeded = new HashMap<ItemStack, EquipmentSlot>();

    public Minigame(String name, String description, long duration, GameMode mode, MinigameType type) {

        this.name = name;
        this.description = description;
        this.duration = duration;
        this.mode = mode;
        this.type = type;

    }

    public void init() {

        if(!itemsNeeded.isEmpty()) {

            for(ItemStack stack : itemsNeeded.keySet()) {

                EquipmentSlot slot = itemsNeeded.get(stack);

                for(Player player : playing) {

                    PlayerInventory i = player.getInventory();

                    i.clear();

                    ItemStack r = i.getItem(slot);

                    r.setItemMeta(stack.getItemMeta());

                    player.setGameMode(mode);

                }

            }

        }

    }

    public void addSpectator(Player player) {

        player.setGameMode(GameMode.SPECTATOR);

        Bukkit.broadcastMessage("spectator is " + player.getName());

    }

    public void teleportAllPlayersToWorldSpawn() {

        for(Player player : playing) {

            player.teleport(player.getWorld().getSpawnLocation());

        }

    }

    public void teleportAllPlayersToLocation(Location location) {

        for(Player player : playing) {

            player.teleport(player.getWorld().getSpawnLocation());

        }

    }

    public void addAllOnlinePlayers() {

        for(Player player : Bukkit.getOnlinePlayers()) {

            playing.add(player);

        }

    }

    public abstract void handleAttack(Player attacker, Player hit, EntityDamageByEntityEvent event);
    public abstract void start();
    public abstract void stop();

}
