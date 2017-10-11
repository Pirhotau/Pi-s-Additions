package com.Pirhotau.PiAdditions.Items;

import com.Pirhotau.PiAdditions.Crafting.PCreativeTab;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;

public abstract class PItem extends Item {
	
	public PItem(String name) {
		this.setUnlocalizedName(name);
		this.setCreativeTab(PCreativeTab.INSTANCE);
		this.setMaxStackSize(64);
		
		this.setRegistryName(name);
	}
	
	/**
	 * Used to register the current item
	 * 
	 * @param registry
	 */
	public void register(IForgeRegistry<Item> registry) {
		registry.register(this);
	}
	
	/**
	 * Used to register the item model
	 */
	@SideOnly(Side.CLIENT)
	public void registerModel() {
		ModelResourceLocation mrl = new ModelResourceLocation(this.getRegistryName().toString(), "inventory");
		ModelLoader.setCustomModelResourceLocation(this, 0, mrl);
	}
	
}
