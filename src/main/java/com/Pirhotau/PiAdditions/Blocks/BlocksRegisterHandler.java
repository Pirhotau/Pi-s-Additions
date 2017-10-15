package com.Pirhotau.PiAdditions.Blocks;

import com.Pirhotau.PiAdditions.Blocks.Decoration.Barrier.BlockBarrier;
import com.Pirhotau.PiAdditions.Blocks.Decoration.Barrier.ItemBlockBarrier;
import com.Pirhotau.PiAdditions.Blocks.Decoration.LadderProtection.BlockLadderProtection;
import com.Pirhotau.PiAdditions.Blocks.Decoration.LadderProtection.ItemBlockLadderProtection;
import com.Pirhotau.PiAdditions.Blocks.Decoration.MultiblockGateway.BlockMultiblockGateway;
import com.Pirhotau.PiAdditions.Blocks.Decoration.MultiblockLadder.BlockMultiblockLadder;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber
public final class BlocksRegisterHandler {	
	/* List of all declared blocks */
	@GameRegistry.ObjectHolder("piadditions:barrier")
	public static PBlock BARRIER = new BlockBarrier();
	
	@GameRegistry.ObjectHolder("piadditions:multiblockgateway")
	public static PBlock MULTIBLOCK_GATEWAY = new BlockMultiblockGateway();
	
	@GameRegistry.ObjectHolder("piadditions:multiblockladder")
	public static PBlock MULTIBLOCK_LADDER = new BlockMultiblockLadder();
	
	@GameRegistry.ObjectHolder("piadditions:ladder_protection")
	public static PBlock LADDER_PROTECTION = new BlockLadderProtection();
	
	
	
	public static ItemBlock ITEMBLOCK_BARRIER;
	public static ItemBlock ITEMBLOCK_LADDER_PROTECTION;
	

	/**
	 * Register blocks
	 * @param event
	 */
	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event) {
		IForgeRegistry<Block> registry = event.getRegistry();
		
		BARRIER.register(registry);
		MULTIBLOCK_GATEWAY.register(registry);
		
		LADDER_PROTECTION.register(registry);
		MULTIBLOCK_LADDER.register(registry);
		
		initializeItemBlocks();
	}
	
	/**
	 * Register ItemBlock
	 * @param event
	 */
	@SubscribeEvent
	public static void registerItemBlocks(RegistryEvent.Register<Item> event) {
		IForgeRegistry<Item> registry = event.getRegistry();
		
		BARRIER.registerItemBlock(registry);
		MULTIBLOCK_GATEWAY.registerItemBlock(registry);
		
		LADDER_PROTECTION.registerItemBlock(registry);
		MULTIBLOCK_LADDER.registerItemBlock(registry);
	}
	
	public static void initializeItemBlocks() {
		ITEMBLOCK_BARRIER = new ItemBlockBarrier(BARRIER);
		ITEMBLOCK_LADDER_PROTECTION = new ItemBlockLadderProtection(LADDER_PROTECTION);
	}

}
