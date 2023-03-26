package com.trxsh.minigames.executor;

import com.trxsh.minigames.games.Minigame;
import com.trxsh.minigames.games.MinigameType;
import com.trxsh.minigames.handler.MinigameHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class StartMinigameExecutor implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(command.getName().equalsIgnoreCase("start")) {

            if(args.length == 0) {

                sender.sendMessage("you must mark a game to play");
                return true;

            } else {

                Minigame minigame = MinigameHandler.getMinigame(args[0]);

                MinigameHandler.startMinigame(minigame);

                return true;

            }

        }

        return false;

    }
}
