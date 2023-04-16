package com.trxsh.minigames.utility;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ItemUtility {

    public static ItemStack to64(Material s) {

        ItemStack stack = new ItemStack(s);

        stack.setAmount(64);

        return stack;

    }

    public static ItemStack toStack(Material s) {

        ItemStack stack = new ItemStack(s);

        return stack;

    }

}
