package com.trxsh.minigames.executor;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.world.World;
import com.trxsh.minigames.handler.PostGameHandler;
import com.trxsh.minigames.utility.FakePlayerUtility;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

public class FakePlayerExecutor implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(command.getName().equalsIgnoreCase("spawnfakeplayer")) {

            //FakePlayerUtility.AddFakePlayer(UUID.randomUUID(), (Player)sender, ((Player)sender).getLocation(), false);
            //PostGameHandler.startPostGame(null, null, (Player)sender);

            ClipboardFormat format = ClipboardFormats.findByFile(new File("podium.schematic"));
            try (ClipboardReader reader = format.getReader(new FileInputStream(new File("podium.schematic")))) {

                Clipboard clipboard = reader.read();

                try (EditSession editSession = WorldEdit.getInstance().newEditSession(BukkitAdapter.adapt(((Player)sender).getWorld()))) {
                    Operation operation = new ClipboardHolder(clipboard)
                            .createPaste(editSession)
                            .to(BlockVector3.at(0, 10, 0))
                            .ignoreAirBlocks(false)
                            .build();
                    Operations.complete(operation);
                } catch (WorldEditException e) {
                    e.printStackTrace();
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        return false;

    }
}
