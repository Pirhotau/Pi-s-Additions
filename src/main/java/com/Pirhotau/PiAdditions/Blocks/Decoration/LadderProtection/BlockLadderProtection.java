package com.Pirhotau.PiAdditions.Blocks.Decoration.LadderProtection;

import java.util.List;

import com.Pirhotau.PiAdditions.Blocks.BlocksRegisterHandler;
import com.Pirhotau.PiAdditions.Blocks.PBlock;
import com.Pirhotau.PiAdditions.Items.ItemsRegisterHandler;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;

public class BlockLadderProtection extends PBlock {

	public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
	
	public static final AxisAlignedBB AABB_LADDER_PROTECTION_NORTH = new AxisAlignedBB(0.0D,	0.0D, 	0.875D, 	1.0D, 	1.0D, 	1.0D);
	public static final AxisAlignedBB AABB_LADDER_PROTECTION_SOUTH = new AxisAlignedBB(0.0D,	0.0D, 	0.0D, 		1.0D, 	1.0D, 	0.125D);
	public static final AxisAlignedBB AABB_LADDER_PROTECTION_EAST = new AxisAlignedBB (0.0D,	0.0D, 	0.0D,	 	0.125D, 1.0D, 	1.0D);
	public static final AxisAlignedBB AABB_LADDER_PROTECTION_WEST = new AxisAlignedBB (0.875D,	0.0D, 	0.0D, 		1.0D, 	1.0D, 	1.0D);
	
	public BlockLadderProtection() {
		super("ladder_protection");
		this.setDefaultState(this.blockState.getBaseState()
				.withProperty(FACING, EnumFacing.NORTH));
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] {FACING});
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(FACING).getHorizontalIndex();
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta));
	}
	
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer,
			ItemStack stack) {
		worldIn.setBlockState(pos, state.withProperty(FACING, placer.getHorizontalFacing()));
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if(!worldIn.isRemote) {
			if(playerIn.isSneaking() && playerIn.getHeldItemMainhand().getItem() == ItemsRegisterHandler.WRENCH) {
				this.breakBlock(worldIn, pos, state);
				EntityItem item = new EntityItem(worldIn, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(BlocksRegisterHandler.LADDER_PROTECTION, 1));
				worldIn.spawnEntity(item);
				worldIn.setBlockToAir(pos);
				return true;
			}
		}
		
		return false;
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.SOLID;
	}
	
	public boolean isFullCube(IBlockState state)
    {
        return false;
    }
	
	@Override
	public boolean isFullBlock(IBlockState state) {
		return false;
	}
	
	@Override
	public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox,
			List<AxisAlignedBB> collidingBoxes, Entity entityIn, boolean p_185477_7_) {
		switch(state.getValue(FACING)) {
			case NORTH: {
				addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_LADDER_PROTECTION_SOUTH);
				addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_LADDER_PROTECTION_EAST);
				addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_LADDER_PROTECTION_WEST);
				break;
			}
			case SOUTH: {
				addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_LADDER_PROTECTION_NORTH);
				addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_LADDER_PROTECTION_EAST);
				addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_LADDER_PROTECTION_WEST);
				break;
			}
			case EAST: {
				addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_LADDER_PROTECTION_NORTH);
				addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_LADDER_PROTECTION_SOUTH);
				addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_LADDER_PROTECTION_WEST);
				break;
			}
			case WEST: {
				addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_LADDER_PROTECTION_NORTH);
				addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_LADDER_PROTECTION_SOUTH);
				addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_LADDER_PROTECTION_EAST);
				break;
			}
			default: {
				addCollisionBoxToList(pos, entityBox, collidingBoxes, FULL_BLOCK_AABB);
			}
		}
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return FULL_BLOCK_AABB;
	}
	
	@Override
	public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos,
			EnumFacing side) {
		return true;
	}
	
	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.MODEL;
	}
	
	@Override
	public void registerItemBlock(IForgeRegistry<Item> registry) {
		registry.register(BlocksRegisterHandler.ITEMBLOCK_LADDER_PROTECTION);
	}
	
}
