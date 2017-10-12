package com.Pirhotau.PiAdditions.Client.Render.Blocks;

import com.Pirhotau.PiAdditions.Blocks.BlocksRegisterHandler;

import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
@EventBusSubscriber
public final class BlocksRenderRegister {
	
	/**
	 * Register blocks models
	 */
	@SubscribeEvent
	public static void registerBlocksRenderer(ModelRegistryEvent event) {
		BlocksRegisterHandler.GRID.registerModel();
		BlocksRegisterHandler.BARRIER.registerModel();
		BlocksRegisterHandler.MULTIBLOCK_GATEWAY.registerModel();
	}
}
