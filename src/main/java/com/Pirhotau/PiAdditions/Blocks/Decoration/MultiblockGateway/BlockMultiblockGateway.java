package com.Pirhotau.PiAdditions.Blocks.Decoration.MultiblockGateway;

import java.util.List;
import java.util.Locale;

import com.Pirhotau.PiAdditions.Blocks.BlocksRegisterHandler;
import com.Pirhotau.PiAdditions.Blocks.PBlockTileEntity;
import com.Pirhotau.PiAdditions.Blocks.Decoration.MultiblockLadder.BlockMultiblockLadder;
import com.Pirhotau.PiAdditions.Items.ItemsRegisterHandler;

import net.minecraft.block.Block;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
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
import net.minecraft.util.IStringSerializable;
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
	public static final PropertyEnum<CustomEnumFacingLadder> LADDER = PropertyEnum.create("ladder", CustomEnumFacingLadder.class);
	
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
				.withProperty(LADDER, CustomEnumFacingLadder.NONE)
			);
	}

	/*
	 * ------------- ORIENTATION
	 */
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] {GRID_FACING, BARRIER_NORTH, BARRIER_SOUTH, BARRIER_EAST, BARRIER_WEST, LADDER});
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
					.withProperty(BARRIER_WEST, this.getBarrier(worldIn, pos, EnumFacing.WEST))
					.withProperty(LADDER, this.getLadder(worldIn, pos));
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		
		if(!worldIn.isRemote) {
			// Tries to place the barrier
			if(!playerIn.isSneaking() && playerIn.getHeldItemMainhand().getItem() == Item.getItemFromBlock(BlocksRegisterHandler.BARRIER) && !this.getBarrier(worldIn, pos, playerIn.getHorizontalFacing()) && !this.getLadder(worldIn, pos).getName().equals(playerIn.getHorizontalFacing().getOpposite().getName())) {
				if(!playerIn.isCreative()) playerIn.getHeldItemMainhand().shrink(1);
				this.setBarrier(worldIn, pos, playerIn.getHorizontalFacing(), true);
				return true;
			}
			// Tries to place a ladder
			else if(!playerIn.isSneaking() && playerIn.getHeldItemMainhand().getItem() == Item.getItemFromBlock(BlocksRegisterHandler.MULTIBLOCK_LADDER) && !this.getBarrier(worldIn, pos, playerIn.getHorizontalFacing()) && this.getLadder(worldIn, pos) == CustomEnumFacingLadder.NONE) {
				if(!playerIn.isCreative()) playerIn.getHeldItemMainhand().shrink(1);
				this.setLadder(worldIn, pos, CustomEnumFacingLadder.getCustomEnumFacingLadderFromHorizontalEnumFacing(playerIn.getHorizontalFacing().getOpposite()));
				return true;
			}
			// Rotates the block
			else if(!playerIn.isSneaking() && hand == EnumHand.MAIN_HAND && playerIn.getHeldItemMainhand().getItem() == ItemsRegisterHandler.WRENCH) {
				this.rotateBlock(worldIn, pos, EnumFacing.DOWN);
				return true;
			}
			// Tries to remove the barrier
			else if(playerIn.isSneaking() && playerIn.getHeldItemMainhand().getItem() == ItemsRegisterHandler.WRENCH && this.getBarrier(worldIn, pos, playerIn.getHorizontalFacing())) {
				this.setBarrier(worldIn, pos, playerIn.getHorizontalFacing(), false);
				EntityItem item = new EntityItem(worldIn, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(BlocksRegisterHandler.BARRIER, 1));
				worldIn.spawnEntity(item);
				return true;
			}
			// Tries to remove the ladder
			else if(playerIn.isSneaking() && playerIn.getHeldItemMainhand().getItem() == ItemsRegisterHandler.WRENCH && this.getLadder(worldIn, pos).getName().equals(playerIn.getHorizontalFacing().getOpposite().getName())) {
				this.setLadder(worldIn, pos, CustomEnumFacingLadder.NONE);
				EntityItem item = new EntityItem(worldIn, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(BlocksRegisterHandler.MULTIBLOCK_LADDER, 1));
				worldIn.spawnEntity(item);
				return true;
			}
			// Tries to remove the grid
			else if(playerIn.isSneaking() && playerIn.getHeldItemMainhand().getItem() == ItemsRegisterHandler.WRENCH && this.getBarrierNumber(worldIn, pos) == 0) {
				this.breakBlock(worldIn, pos, state);
				EntityItem item = new EntityItem(worldIn, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(BlocksRegisterHandler.MULTIBLOCK_GATEWAY, 1));
				worldIn.spawnEntity(item);
				worldIn.setBlockToAir(pos);
				return true;
			}
		}
		
		return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
	}
	
	@Override
	public boolean isLadder(IBlockState state, IBlockAccess world, BlockPos pos, EntityLivingBase entity) {
		return this.getLadder(world, pos) != CustomEnumFacingLadder.NONE;
	}
	
	public EnumFacing getFacing(IBlockAccess world, BlockPos pos) {
		TileEntityMultiblockGateway te = this.getTileEntity(world, pos);
		return te != null ? te.getFacing() : EnumFacing.NORTH;
	}
	
	public boolean getBarrier(IBlockAccess world, BlockPos pos, EnumFacing facing) {
		TileEntityMultiblockGateway te = this.getTileEntity(world, pos);
		return te != null ? te.getBarrier(facing) : false;
	}
	
	public CustomEnumFacingLadder getLadder(IBlockAccess world, BlockPos pos) {
		TileEntityMultiblockGateway te = this.getTileEntity(world, pos);
		return te != null ? te.getLadder() : CustomEnumFacingLadder.NONE;
	}
	
	public void setFacing(IBlockAccess world, BlockPos pos, EnumFacing facing) {
		TileEntityMultiblockGateway te = this.getTileEntity(world, pos);
		if(te != null) te.setFacing(facing);
	}
	
	public void setBarrier(IBlockAccess world, BlockPos pos, EnumFacing facing, boolean exists) {
		TileEntityMultiblockGateway te = this.getTileEntity(world, pos);
		if(te != null) te.setBarrier(facing, exists);
	}
	
	public void setLadder(IBlockAccess world, BlockPos pos, CustomEnumFacingLadder ladder) {
		TileEntityMultiblockGateway te = this.getTileEntity(world, pos);
		if(te != null) te.setLadder(ladder);
	}
	
	public int getBarrierNumber(IBlockAccess world, BlockPos pos) {
		TileEntityMultiblockGateway te = this.getTileEntity(world, pos);
		return te != null ? te.getBarrierNumber() : 0;
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
		
		switch(this.getLadder(worldIn, pos)) {
		case NONE: break;
		case NORTH: {
			addCollisionBoxToList(pos, entityBox, collidingBoxes, BlockMultiblockLadder.AABB_LADDER_NORTH);
			break;
		}
		case SOUTH: {
			addCollisionBoxToList(pos, entityBox, collidingBoxes, BlockMultiblockLadder.AABB_LADDER_SOUTH);
			break;
		}
		case EAST: {
			addCollisionBoxToList(pos, entityBox, collidingBoxes, BlockMultiblockLadder.AABB_LADDER_EAST);
			break;
		}
		case WEST: {
			addCollisionBoxToList(pos, entityBox, collidingBoxes, BlockMultiblockLadder.AABB_LADDER_WEST);
			break;
		}
		}
	}
	
	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		this.releaseItems(worldIn, pos);
		super.breakBlock(worldIn, pos, state);
	}
	
	private void releaseItems(World worldIn, BlockPos pos) {
		int number = this.getBarrierNumber(worldIn, pos);
		if(number != 0) {
			EntityItem item = new EntityItem(worldIn, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(BlocksRegisterHandler.BARRIER, number));
			worldIn.spawnEntity(item);
		}
		
		if(this.getLadder(worldIn, pos) != CustomEnumFacingLadder.NONE) {
			EntityItem item = new EntityItem(worldIn, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(BlocksRegisterHandler.MULTIBLOCK_LADDER, number));
			worldIn.spawnEntity(item);
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
		if(source.getBlockState(pos).getBlock() != BlocksRegisterHandler.MULTIBLOCK_GATEWAY) {
			return NULL_AABB;
		}
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
	
	
	public enum CustomEnumFacingLadder implements IStringSerializable {
		NONE(-1, -1, "none"),
		NORTH(0, 1, "north"),
		SOUTH(1, 0, "south"),
		EAST(2, 3, "east"),
		WEST(3, 2, "west");
		
		/** Ordering index: NONE-N-S-W-E */
		private final int index;
		/** Index of the opposite Facing in the VALUES array */
		private final int opposite;
		private final String name;
		public static final CustomEnumFacingLadder[] VALUES = new CustomEnumFacingLadder[4];
		
		private CustomEnumFacingLadder(int indexIn, int oppositeIn, String nameIn) {
			this.index = indexIn;
			this.opposite = oppositeIn;
			this.name = nameIn;
		}
		
		public int getIndex() {
			return this.index;
		}
		
		public CustomEnumFacingLadder getOpposite() {
			return this.opposite != NONE.opposite ? VALUES[this.opposite] : NONE;
		}
		
		@Override
		public String getName() {
			return this.name;
		}
		
		public static CustomEnumFacingLadder getValue(int index) {
			return index != NONE.getIndex() ? VALUES[index] : NONE;
		}
		
		public static CustomEnumFacingLadder getCustomEnumFacingLadderFromHorizontalEnumFacing(EnumFacing facing) {
			switch(facing) {
			case NORTH: return NORTH;
			case SOUTH: return SOUTH;
			case EAST: return EAST;
			case WEST: return WEST;
			default: return NONE;
			}
		}
		
		
		static
	    {
	        for (CustomEnumFacingLadder enumFacing : values())
	        {
	            if(enumFacing.index != NONE.index) VALUES[enumFacing.index] = enumFacing;
	        }
	    }
	}
}
