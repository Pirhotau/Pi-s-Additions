package com.Pirhotau.PiAdditions.Client.Render.Items;

import com.Pirhotau.PiAdditions.Items.ItemsRegisterHandler;

import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber
public final class ItemsRenderRegister {
	/**
	 * Register item models
	 * 
	 * @param event
	 */
	@SubscribeEvent
	public static void registerItemRenderer(ModelRegistryEvent event) {
		ItemsRegisterHandler.WRENCH.registerModel();
	}
}
