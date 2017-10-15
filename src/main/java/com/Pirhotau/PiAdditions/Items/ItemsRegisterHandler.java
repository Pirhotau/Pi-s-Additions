package com.Pirhotau.PiAdditions.Items;

import com.Pirhotau.PiAdditions.Items.IronPlate.ItemIronPlate;
import com.Pirhotau.PiAdditions.Items.IronTube.ItemIronTube;
import com.Pirhotau.PiAdditions.Items.Wrench.ItemWrench;

import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber
public final class ItemsRegisterHandler {
	
	/* List of all items which need to be registered */
	@GameRegistry.ObjectHolder("piadditions:wrench")
	public static PItem WRENCH = new ItemWrench();
	
	@GameRegistry.ObjectHolder("piadditions:iron_tube")
	public static PItem IRON_TUBE = new ItemIronTube();
	
	@GameRegistry.ObjectHolder("piadditions:iron_plate")
	public static PItem IRON_PLATE = new ItemIronPlate();
	
	/**
	 * Register items
	 * 
	 * @param event
	 */
	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event) {
		IForgeRegistry<Item> registry = event.getRegistry();
		
		WRENCH.register(registry);
		IRON_TUBE.register(registry);
		IRON_PLATE.register(registry);
	}
}
