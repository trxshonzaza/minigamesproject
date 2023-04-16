package com.trxsh.minigames.utility;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.datafixers.util.Pair;
import com.trxsh.minigames.Main;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.server.network.ServerPlayerConnection;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.ProfilePublicKey;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_19_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_19_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class FakePlayerUtility {

    public static HashMap<UUID, ServerPlayer> fakePlayers = new HashMap<UUID, ServerPlayer>();

    public static void AddFakePlayer(UUID id, Player player, Location location, boolean useHelmet) {

        CraftPlayer craftPlayer = (CraftPlayer)player;
        ServerPlayer serverPlayer = craftPlayer.getHandle();

        MinecraftServer server = serverPlayer.getServer();

        ServerLevel level = serverPlayer.getLevel();

        GameProfile profile = new GameProfile(id, "");

        ProfilePublicKey key = new ProfilePublicKey(null);

        Property property = ((CraftPlayer)player).getProfile().getProperties().get("textures").iterator().next();

        String texture = property.getValue();
        String signature = property.getSignature();

        profile.getProperties().put("textures", new Property("textures", texture, signature));

        ServerPlayer npc = new ServerPlayer(server, level, profile, key);

        npc.setPos(location.getX(), location.getY(), location.getZ());

        for(Player player1 : Bukkit.getOnlinePlayers()) {

            ServerGamePacketListenerImpl sgPacket = ((CraftPlayer)player1).getHandle().connection;

            sgPacket.send(new ClientboundPlayerInfoPacket(ClientboundPlayerInfoPacket.Action.ADD_PLAYER, npc));
            sgPacket.send(new ClientboundAddPlayerPacket(npc));
            sgPacket.send(new ClientboundAddEntityPacket(npc));
            sgPacket.send(new ClientboundRotateHeadPacket(npc, (byte)((serverPlayer.getBukkitYaw()))));

            if(useHelmet) {

                ItemStack helmet = new ItemStack(Material.GOLDEN_HELMET);
                sgPacket.send(new ClientboundSetEquipmentPacket(npc.getBukkitEntity().getEntityId(), List.of(new Pair<>(EquipmentSlot.HEAD, CraftItemStack.asNMSCopy(helmet)))));

            }

        }

    }

    public void removeAllNPCS() {

        for(ServerPlayer npc : fakePlayers.values()) {

            for(Player player : Bukkit.getOnlinePlayers()) {

                ServerGamePacketListenerImpl sgPacket = ((CraftPlayer)player).getHandle().connection;

                sgPacket.send(new ClientboundRemoveEntitiesPacket(npc.getBukkitEntity().getEntityId()));
                sgPacket.send(new ClientboundPlayerInfoPacket(ClientboundPlayerInfoPacket.Action.REMOVE_PLAYER, npc));

            }

        }

        fakePlayers.clear();

    }

}
