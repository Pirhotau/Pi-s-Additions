package com.Pirhotau.PiAdditions.Blocks.Decoration.MultiblockGateway;

import java.util.List;

import com.Pirhotau.PiAdditions.Blocks.BlocksRegisterHandler;
import com.Pirhotau.PiAdditions.Blocks.PBlockTileEntity;
import com.Pirhotau.PiAdditions.Items.ItemsRegisterHandler;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
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

public class BlockMultiblockGateway extends PBlockTileEntity<TileEntityMultiblockGateway> {
	
	public static final PropertyDirection GRID_FACING = PropertyDirection.create("grid_facing", EnumFacing.Plane.HORIZONTAL);
	public static final PropertyBool BARRIER_NORTH = PropertyBool.create("barrier_north");
	public static final PropertyBool BARRIER_SOUTH = PropertyBool.create("barrier_south");
	public static final PropertyBool BARRIER_EAST = PropertyBool.create("barrier_east");
	public static final PropertyBool BARRIER_WEST = PropertyBool.create("barrier_west");
	
	public static final AxisAlignedBB AABB_GRID = new AxisAlignedBB(0.0D, 0.0D, 0D, 1.0D, 0.125D, 1.0D);
	public static final AxisAlignedBB AABB_BARRIER_NORTH = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.125D);
	public static final AxisAlignedBB AABB_BARRIER_SOUTH = new AxisAlignedBB(0.0D, 0.0D, 0.875D, 1.0D, 1.0D, 1.0D);
	public static final AxisAlignedBB AABB_BARRIER_EAST = new AxisAlignedBB(0.875D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
	public static final AxisAlignedBB AABB_BARRIER_WEST = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.125D, 1.0D, 1.0D);

	public BlockMultiblockGateway() {
		super("multiblock_gateway");
		this.setDefaultState(this.getDefaultState()
				.withProperty(GRID_FACING, EnumFacing.NORTH)
				.withProperty(BARRIER_NORTH, false)
				.withProperty(BARRIER_SOUTH, false)
				.withProperty(BARRIER_EAST, false)
				.withProperty(BARRIER_WEST, false)
			);
	}

	/*
	 * ------------- ORIENTATION
	 */
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] {GRID_FACING, BARRIER_NORTH, BARRIER_SOUTH, BARRIER_EAST, BARRIER_WEST});
	}
	
	
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		worldIn.setBlockState(pos, state.withProperty(GRID_FACING, placer.getHorizontalFacing().getOpposite()));
		TileEntityMultiblockGateway te = this.getTileEntity(worldIn, pos);
		te.setFacing(placer.getHorizontalFacing().getOpposite());
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
		return 0;
	}
	
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		return state.withProperty(GRID_FACING, this.getFacing(worldIn, pos))
					.withProperty(BARRIER_NORTH, this.getBarrier(worldIn, pos, EnumFacing.NORTH))
					.withProperty(BARRIER_SOUTH, this.getBarrier(worldIn, pos, EnumFacing.SOUTH))
					.withProperty(BARRIER_EAST, this.getBarrier(worldIn, pos, EnumFacing.EAST))
					.withProperty(BARRIER_WEST, this.getBarrier(worldIn, pos, EnumFacing.WEST));
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if(!worldIn.isRemote) {
			if(hand == EnumHand.MAIN_HAND) {				
				if(playerIn.getHeldItemMainhand().getItem() == Item.getItemFromBlock(BlocksRegisterHandler.BARRIER)) {
					if(!this.getBarrier(worldIn, pos, playerIn.getHorizontalFacing())) {
						playerIn.setHeldItem(hand, playerIn.getHeldItemMainhand().splitStack(1));
						this.setBarrier(worldIn, pos, playerIn.getHorizontalFacing(), true);
					}
					return true;
				} else if(playerIn.getHeldItemMainhand().getItem() == ItemsRegisterHandler.WRENCH) {
					return this.rotateBlock(worldIn, pos, facing);
				} else return false;
			} else if(playerIn.isSneaking() && playerIn.getHeldItemMainhand().getItem() == ItemsRegisterHandler.WRENCH) {
				if(this.getBarrier(worldIn, pos, playerIn.getHorizontalFacing())) {
					this.setBarrier(worldIn, pos, playerIn.getHorizontalFacing(), false);
					
					EntityItem item = new EntityItem(worldIn, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(BlocksRegisterHandler.BARRIER, 1));
					worldIn.spawnEntity(item);
				}
				return true;
			}
			
			else return false;
		}
		return false;
	}
	
	public EnumFacing getFacing(IBlockAccess world, BlockPos pos) {
		TileEntityMultiblockGateway te = this.getTileEntity(world, pos);
		return te != null ? te.getFacing() : EnumFacing.NORTH;
	}
	
	public boolean getBarrier(IBlockAccess world, BlockPos pos, EnumFacing facing) {
		TileEntityMultiblockGateway te = this.getTileEntity(world, pos);
		return te != null ? te.getBarrier(facing) : false;
	}
	
	public void setFacing(IBlockAccess world, BlockPos pos, EnumFacing facing) {
		TileEntityMultiblockGateway te = this.getTileEntity(world, pos);
		if(te != null) te.setFacing(facing);
	}
	
	public void setBarrier(IBlockAccess world, BlockPos pos, EnumFacing facing, boolean exists) {
		TileEntityMultiblockGateway te = this.getTileEntity(world, pos);
		if(te != null) te.setBarrier(facing, exists);
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
		addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_GRID);
		
		if(this.getBarrier(worldIn, pos, EnumFacing.NORTH)) {
			addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_BARRIER_NORTH);
		}
		if(this.getBarrier(worldIn, pos, EnumFacing.SOUTH)) {
			addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_BARRIER_SOUTH);
		}
		if(this.getBarrier(worldIn, pos, EnumFacing.EAST)) {
			addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_BARRIER_EAST);
		}
		if(this.getBarrier(worldIn, pos, EnumFacing.WEST)) {
			addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_BARRIER_WEST);
		}
	}
	
	
	
	/*
	 * ------------ Transparency
	 */
	
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
	
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
	     return new AxisAlignedBB(0.0D, 0.0D, 0D, 1.0D, 0.125D, 1.0D);
	}
	
	@Override
	public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos,
			EnumFacing side) {
		IBlockState iblockstate = blockAccess.getBlockState(pos.offset(side));
        Block block = iblockstate.getBlock();

        if (block == this) {
            return false;
        }
        
		return true;
	}
	
	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.MODEL;
	}
	
	/*
	 * ------------- Tile entity
	 */
	@Override
	public Class<TileEntityMultiblockGateway> getTileEntityClass() {
		return TileEntityMultiblockGateway.class;
	}

	@Override
	public TileEntityMultiblockGateway createTileEntity(World world, IBlockState state) {
		return new TileEntityMultiblockGateway();
	}
}
