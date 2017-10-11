package com.Pirhotau.PiAdditions.Blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

public abstract class PBlockTileEntity<TE extends TileEntity> extends PBlock {
	/**
	 * Full constructor for a tile entity
	 * 
	 * @param material
	 * @param name
	 * @param hardness
	 * @param resistance
	 * @param creativeTabs
	 */
	public PBlockTileEntity(Material material, String name, float hardness, float resistance, CreativeTabs creativeTabs) {
		super(material, name, hardness, resistance, creativeTabs);
	}
	
	/**
	 * Basic constructor for a tile entity
	 * 
	 * @param name
	 */
	public PBlockTileEntity(String name) {
		super(name);
	}

	
	
	/* (non-Javadoc)
	 * @see com.Pirhotau.PiAdditions.Blocks.PBlock#register(net.minecraftforge.registries.IForgeRegistry)
	 * Automatically register the tile entity
	 */
	@Override
	public void register(IForgeRegistry<Block> registry) {
		super.register(registry);
		GameRegistry.registerTileEntity(this.getTileEntityClass(), this.getRegistryName().toString());
	}
	
	public abstract Class<TE> getTileEntityClass();
	
	@SuppressWarnings("unchecked")
	public TE getTileEntity(IBlockAccess world, BlockPos pos) {
		return (TE)world.getTileEntity(pos);
	}
	
	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}
	
	@Override
	public abstract TE createTileEntity(World world, IBlockState state);
	
	
}
