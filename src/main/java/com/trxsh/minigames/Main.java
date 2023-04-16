package com.trxsh.minigames;

import com.trxsh.minigames.executor.FakePlayerExecutor;
import com.trxsh.minigames.executor.StartMinigameExecutor;
import com.trxsh.minigames.executor.StartMinigameTabCompleter;
import com.trxsh.minigames.games.*;
import com.trxsh.minigames.handler.MinigameHandler;
import com.trxsh.minigames.listener.PlayerAttackListener;
import com.trxsh.minigames.listener.PlayerJoinListener;
import com.trxsh.minigames.listener.PlayerMoveListener;
import com.trxsh.minigames.utility.ItemUtility;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public final class Main extends JavaPlugin {

    public static Main instance;

    public static HashMap<ItemStack, EquipmentSlot> snipersItems = new HashMap<>();
    static {

        snipersItems.put(ItemUtility.to64(Material.ARROW), EquipmentSlot.OFF_HAND);
        snipersItems.put(ItemUtility.toStack(Material.BOW), EquipmentSlot.HAND);

    }

    @Override
    public void onEnable() {

        Bukkit.getPluginManager().registerEvents(new PlayerAttackListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerMoveListener(), this);

        Bukkit.getPluginCommand("start").setExecutor(new StartMinigameExecutor());
        Bukkit.getPluginCommand("spawnfakeplayer").setExecutor(new FakePlayerExecutor());

        Bukkit.getPluginCommand("start").setTabCompleter(new StartMinigameTabCompleter());

        MinigameHandler.addMinigame(new MinigameTag("Tag", "whoever is it has to tag other players!", 60L, GameMode.ADVENTURE, MinigameType.TAG));
        MinigameHandler.addMinigame(new MinigameManhunt("Manhunt", "whoever is it has to tag all players! Every player that is tagged is with the Tagged team!", 1L, GameMode.ADVENTURE, MinigameType.MANHUNT));
        MinigameHandler.addMinigame(new MinigameTntTag("TNT Tag", "whoever is it has to tag other players! whoever has TNT on their head will die!", 1L, GameMode.ADVENTURE, MinigameType.TNT_TAG));
        MinigameHandler.addMinigame(new MinigameFreezeTag("Freeze Tag", "whoever is it has to tag other players and will freeze! other players will ", 180L, GameMode.ADVENTURE, MinigameType.FREEZE_TAG));
        MinigameHandler.addMinigame(new MinigameSnipers("Snipers", "shoot other players to gain points!", 60L, GameMode.ADVENTURE, MinigameType.SNIPERS, snipersItems));

        instance = this;
    }

    @Override
    public void onDisable() {



    }
}
