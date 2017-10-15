package com.Pirhotau.PiAdditions.Blocks.Decoration.Barrier;

import com.Pirhotau.PiAdditions.Blocks.BlocksRegisterHandler;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemBlockBarrier extends ItemBlock {

	public ItemBlockBarrier(Block block) {
		super(block);
		this.setRegistryName(block.getRegistryName());
	}
	
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand,
			EnumFacing facing, float hitX, float hitY, float hitZ) {
		Block block = worldIn.getBlockState(pos).getBlock();
		if(!player.isSneaking() && block == BlocksRegisterHandler.MULTIBLOCK_GATEWAY) {
			return EnumActionResult.SUCCESS;
		} else {
			return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
		}
	}
}
