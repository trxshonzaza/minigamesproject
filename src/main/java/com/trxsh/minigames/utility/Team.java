package com.trxsh.minigames.utility;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Team {

    TeamType type;
    public List<Player> players = new ArrayList();

    public Team(TeamType type, ArrayList<Player> whosOnTeam) throws IllegalAccessException {

        this.players = whosOnTeam;
        this.type = type;

        for(Player player : whosOnTeam) {

            if(type == TeamType.RED)
                player.setDisplayName(ChatColor.RED + player.getName());

            if(type == TeamType.BLUE)
                player.setDisplayName(ChatColor.BLUE + player.getName());

        }

    }


    public void addPlayer(Player player) {

        players.add(player);

        if(type == TeamType.RED)
            player.setDisplayName(ChatColor.RED + player.getName());

        if(type == TeamType.BLUE)
            player.setDisplayName(ChatColor.BLUE + player.getName());

    }


    public void removePlayer(Player player) {

        players.remove(player);
        player.setDisplayName(player.getName());

    }

    public void removeAllPlayers() {

        for(Player player : players)
            player.setDisplayName(player.getName());

        players.clear();

    }

    public Player getPlayer(Player player) {

        for(Player player1 : players)
            if(player.getUniqueId() == player.getUniqueId())
                return player1;

        throw new IllegalStateException("Cannot find player!");

    }

}
