package com.Pirhotau.PiAdditions.Crafting;

import com.Pirhotau.PiAdditions.Main;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

public class PCreativeTab {
	public static final CreativeTabs INSTANCE = new CreativeTabs(Main.MODID) {

		public ItemStack getTabIconItem() {
			return new ItemStack(Blocks.IRON_BARS);
		};
	};
}
