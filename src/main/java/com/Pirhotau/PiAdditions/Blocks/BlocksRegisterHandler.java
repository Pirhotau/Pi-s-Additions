package com.Pirhotau.PiAdditions.Blocks;

import com.Pirhotau.PiAdditions.Blocks.Decoration.Barrier.BlockBarrier;
import com.Pirhotau.PiAdditions.Blocks.Decoration.Grid.BlockGrid;
import com.Pirhotau.PiAdditions.Blocks.Decoration.MultiblockGateway.BlockMultiblockGateway;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber
public final class BlocksRegisterHandler {	
	/* List of all declared blocks */
	@GameRegistry.ObjectHolder("piadditions:grid")
	public static PBlock GRID = new BlockGrid();
	
	@GameRegistry.ObjectHolder("piadditions:barrier")
	public static PBlock BARRIER = new BlockBarrier();
	
	@GameRegistry.ObjectHolder("piadditions:multiblockgateway")
	public static PBlock MULTIBLOCK_GATEWAY = new BlockMultiblockGateway();
	

	/**
	 * Register blocks
	 * @param event
	 */
	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event) {
		IForgeRegistry<Block> registry = event.getRegistry();
		
		GRID.register(registry);
		BARRIER.register(registry);
		MULTIBLOCK_GATEWAY.register(registry);
	}
	
	/**
	 * Register ItemBlock
	 * @param event
	 */
	@SubscribeEvent
	public static void registerItemBlocks(RegistryEvent.Register<Item> event) {
		IForgeRegistry<Item> registry = event.getRegistry();
		
		GRID.registerItemBlock(registry);
		BARRIER.registerItemBlock(registry);
		MULTIBLOCK_GATEWAY.registerItemBlock(registry);
	}

}
