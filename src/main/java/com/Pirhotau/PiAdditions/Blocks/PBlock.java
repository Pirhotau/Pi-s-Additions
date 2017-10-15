package com.Pirhotau.PiAdditions.Blocks;

import com.Pirhotau.PiAdditions.Crafting.PCreativeTab;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;

public abstract class PBlock extends Block {
	/**
	 * Full constructor
	 * 
	 * @param material
	 * @param name
	 * @param hardness
	 * @param resistance
	 * @param creativeTabs
	 */
	public PBlock(Material material, String name, float hardness, float resistance, CreativeTabs creativeTabs) {
		super(material);
		
		setUnlocalizedName(name);
		setCreativeTab(creativeTabs);
		setHardness(hardness);
		setResistance(resistance);
		setRegistryName(name);
		
		setSoundType(SoundType.METAL);
		
	}
	
	
	/**
	 * Basic constructor
	 * 
	 * Material.IRON
	 * Hardness = 2.0f
	 * Resistance = 10.0f
	 * Creative tab: own mod
	 * 
	 * @param name
	 */
	public PBlock(String name) {
		this(Material.GROUND, name, 2.0f, 10.0f, PCreativeTab.INSTANCE);
	}
	
	/**
	 * Used to register the current block
	 * 
	 * @param registry
	 */
	public void register(IForgeRegistry<Block> registry) {
		registry.register(this);
	}
	
	/**
	 * Register the attached itemblock
	 * 
	 * @param registry
	 */
	public void registerItemBlock(IForgeRegistry<Item> registry) {
		registry.register(new ItemBlock(this).setRegistryName(this.getRegistryName().toString()));
	}
	
	/**
	 * Used to register the model of the current block
	 */
	@SideOnly(Side.CLIENT)
	public void registerModel() {
		ModelResourceLocation mrl = new ModelResourceLocation(this.getRegistryName().toString(), "inventory");
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, mrl);
	}
}
