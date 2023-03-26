package com.trxsh.minigames;

import com.trxsh.minigames.executor.StartMinigameExecutor;
import com.trxsh.minigames.executor.StartMinigameTabCompleter;
import com.trxsh.minigames.games.MinigameTag;
import com.trxsh.minigames.games.MinigameType;
import com.trxsh.minigames.handler.MinigameHandler;
import com.trxsh.minigames.listener.PlayerAttackListener;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    public static Main instance;

    @Override
    public void onEnable() {

        Bukkit.getPluginManager().registerEvents(new PlayerAttackListener(), this);

        Bukkit.getPluginCommand("start").setExecutor(new StartMinigameExecutor());
        Bukkit.getPluginCommand("start").setTabCompleter(new StartMinigameTabCompleter());

        MinigameHandler.addMinigame(new MinigameTag("Tag", "whoever is it has to tag other players!", 60L, GameMode.ADVENTURE, MinigameType.TAG));

        instance = this;
    }

    @Override
    public void onDisable() {



    }
}