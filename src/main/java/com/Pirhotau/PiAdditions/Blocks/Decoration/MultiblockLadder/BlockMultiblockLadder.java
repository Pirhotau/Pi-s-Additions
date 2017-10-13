package com.Pirhotau.PiAdditions.Blocks.Decoration.MultiblockLadder;

import java.util.List;

import com.Pirhotau.PiAdditions.Blocks.BlocksRegisterHandler;
import com.Pirhotau.PiAdditions.Blocks.PBlockTileEntity;
import com.Pirhotau.PiAdditions.Blocks.Decoration.MultiblockGateway.TileEntityMultiblockGateway;
import com.Pirhotau.PiAdditions.Items.ItemsRegisterHandler;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
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

public class BlockMultiblockLadder extends PBlockTileEntity<TileEntityMultiblockLadder> {
	
	public static final PropertyDirection LADDER_FACING = PropertyDirection.create("ladder_facing", EnumFacing.Plane.HORIZONTAL);
	
	
	public static final AxisAlignedBB AABB_LADDER_NORTH = new AxisAlignedBB(0.1875D,	0.0D, 	0.875D, 	0.8125D, 	1.0D, 	1.0D);
	public static final AxisAlignedBB AABB_LADDER_SOUTH = new AxisAlignedBB(0.1875D,	0.0D, 	0.0D, 		0.8125D, 	1.0D, 	0.125D);
	public static final AxisAlignedBB AABB_LADDER_EAST = new AxisAlignedBB (0.0D,		0.0D, 	0.1875D, 	0.125D, 	1.0D, 	0.8125D);
	public static final AxisAlignedBB AABB_LADDER_WEST = new AxisAlignedBB (0.875D,		0.0D, 	0.1875D, 	1.0D, 		1.0D, 	0.8125D);
	
	public BlockMultiblockLadder() {
		super("multiblock_ladder");
		this.setDefaultState(this.getDefaultState()
				.withProperty(LADDER_FACING, EnumFacing.NORTH)
			);
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] {LADDER_FACING});
	}
	
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer,
			ItemStack stack) {
		worldIn.setBlockState(pos, state.withProperty(LADDER_FACING, placer.getHorizontalFacing().getOpposite()));
		TileEntityMultiblockLadder te = this.getTileEntity(worldIn, pos);
		te.setFacing(placer.getHorizontalFacing().getOpposite());
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
		return 0;
	}
	
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		return state.withProperty(LADDER_FACING, this.getFacing(worldIn, pos));
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if(!worldIn.isRemote) {
			if(hand == EnumHand.MAIN_HAND) {
				if(playerIn.getHeldItemMainhand().getItem() == ItemsRegisterHandler.WRENCH) {
					return this.rotateBlock(worldIn, pos, facing);
				} else return false;
			} else if(playerIn.isSneaking() && playerIn.getHeldItemMainhand().getItem() == ItemsRegisterHandler.WRENCH) {
				this.breakBlock(worldIn, pos, state);
				worldIn.setBlockToAir(pos);
				EntityItem item = new EntityItem(worldIn, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(BlocksRegisterHandler.MULTIBLOCK_LADDER, 1));
				worldIn.spawnEntity(item);
				return true;
			} else return false;
		} else return false;
	}
	
	public EnumFacing getFacing(IBlockAccess world, BlockPos pos) {
		TileEntityMultiblockLadder te = this.getTileEntity(world, pos);
		return te != null ? te.getFacing() : EnumFacing.NORTH;
	}
	
	public void setFacing(IBlockAccess world, BlockPos pos, EnumFacing facing) {
		TileEntityMultiblockLadder te = this.getTileEntity(world, pos);
		if(te != null) te.setFacing(facing);
	}
	
	@Override
	public boolean rotateBlock(World world, BlockPos pos, EnumFacing axis) {
		EnumFacing facing = this.getFacing(world, pos);
		this.setFacing(world, pos, facing.rotateAround(axis.getAxis()));
		return true;
	}
	
	@Override
	public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox,
			List<AxisAlignedBB> collidingBoxes, Entity entityIn, boolean p_185477_7_) {
		if(this.getFacing(worldIn, pos) == EnumFacing.NORTH) {
			addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_LADDER_NORTH);
		} else if(this.getFacing(worldIn, pos) == EnumFacing.SOUTH) {
			addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_LADDER_SOUTH);
		} else if(this.getFacing(worldIn, pos) == EnumFacing.EAST) {
			addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_LADDER_EAST);
		} else if(this.getFacing(worldIn, pos) == EnumFacing.WEST) {
			addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_LADDER_WEST);
		} else {
			addCollisionBoxToList(pos, entityBox, collidingBoxes, FULL_BLOCK_AABB);
		}
	}
	
	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		this.releaseItems(worldIn, pos);
		super.breakBlock(worldIn, pos, state);
	}
	
	private void releaseItems(World worldIn, BlockPos pos) {
		//TODO
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT;
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
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		if(this.getFacing(source, pos) == EnumFacing.NORTH) {
			return AABB_LADDER_NORTH;
		} else if(this.getFacing(source, pos) == EnumFacing.SOUTH) {
			return AABB_LADDER_SOUTH;
		} else if(this.getFacing(source, pos) == EnumFacing.EAST) {
			return AABB_LADDER_EAST;
		} else if(this.getFacing(source, pos) == EnumFacing.WEST) {
			return AABB_LADDER_WEST;
		} else {
			return FULL_BLOCK_AABB;
		}
	}
	
	@Override
	public boolean isLadder(IBlockState state, IBlockAccess world, BlockPos pos, EntityLivingBase entity) {
		return true;
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
	public Class<TileEntityMultiblockLadder> getTileEntityClass() {
		return TileEntityMultiblockLadder.class;
	}

	@Override
	public TileEntityMultiblockLadder createTileEntity(World world, IBlockState state) {
		return new TileEntityMultiblockLadder();
	}
}
