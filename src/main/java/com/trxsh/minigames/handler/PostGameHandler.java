package com.trxsh.minigames.handler;

import com.trxsh.minigames.Main;
import com.trxsh.minigames.utility.FakePlayerUtility;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PostGameHandler {

    public static boolean post = false;

    public static Location forceTPLocation;

    public static World world;

    public static void startPostGame(Player third, Player second, Player first) {

        world = first.getWorld();

        forceTPLocation = new Location(world, 0, 100, 0);

        Bukkit.getScheduler().runTaskLater(Main.instance, new Runnable() {
            @Override
            public void run() {

                post = true;

                Bukkit.getScheduler().runTaskLater(Main.instance, new Runnable() {
                    @Override
                    public void run() {

                        Bukkit.broadcastMessage("3rd place");

                        Bukkit.getScheduler().runTaskLater(Main.instance, new Runnable() {
                            @Override
                            public void run() {

                                Bukkit.broadcastMessage("2nd place");

                                Bukkit.getScheduler().runTaskLater(Main.instance, new Runnable() {
                                    @Override
                                    public void run() {

                                        Bukkit.broadcastMessage("1st place soon");

                                        Bukkit.getScheduler().runTaskLater(Main.instance, new Runnable() {
                                            @Override
                                            public void run() {

                                                Bukkit.broadcastMessage("1st place");

                                                Bukkit.getScheduler().runTaskLater(Main.instance, new Runnable() {
                                                    @Override
                                                    public void run() {

                                                        Bukkit.broadcastMessage("end");
                                                        post = false;

                                                        for(Player pl : Bukkit.getOnlinePlayers())
                                                            pl.teleport(pl.getWorld().getSpawnLocation());

                                                    }
                                                }, 10L * 20L);

                                            }
                                        }, 5L * 20L);

                                    }
                                }, 3L * 20L);

                            }
                        }, 3L * 20L);

                    }
                }, 3L * 20L);

            }
        }, 5L * 20L);

    }

}
