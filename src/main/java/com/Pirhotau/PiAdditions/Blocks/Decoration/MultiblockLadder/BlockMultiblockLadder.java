package com.Pirhotau.PiAdditions.Blocks.Decoration.MultiblockLadder;

import java.util.List;

import com.Pirhotau.PiAdditions.Blocks.BlocksRegisterHandler;
import com.Pirhotau.PiAdditions.Blocks.PBlockTileEntity;
import com.Pirhotau.PiAdditions.Blocks.Decoration.LadderProtection.BlockLadderProtection;
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
import net.minecraftforge.registries.IForgeRegistry;

public class BlockMultiblockLadder extends PBlockTileEntity<TileEntityMultiblockLadder> {
	
	public static final PropertyDirection LADDER_FACING = PropertyDirection.create("ladder_facing", EnumFacing.Plane.HORIZONTAL);
	public static final PropertyBool LADDER_PROTECTION = PropertyBool.create("ladder_protection");
	
	
	public static final AxisAlignedBB AABB_LADDER_NORTH = new AxisAlignedBB(0.1875D,	0.0D, 	0.875D, 	0.8125D, 	1.0D, 	1.0D);
	public static final AxisAlignedBB AABB_LADDER_SOUTH = new AxisAlignedBB(0.1875D,	0.0D, 	0.0D, 		0.8125D, 	1.0D, 	0.125D);
	public static final AxisAlignedBB AABB_LADDER_EAST = new AxisAlignedBB (0.0D,		0.0D, 	0.1875D, 	0.125D, 	1.0D, 	0.8125D);
	public static final AxisAlignedBB AABB_LADDER_WEST = new AxisAlignedBB (0.875D,		0.0D, 	0.1875D, 	1.0D, 		1.0D, 	0.8125D);
	
	
	
	public BlockMultiblockLadder() {
		super("multiblock_ladder");
		this.setDefaultState(this.getDefaultState()
				.withProperty(LADDER_FACING, EnumFacing.NORTH)
				.withProperty(LADDER_PROTECTION, false)
			);
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] {LADDER_FACING, LADDER_PROTECTION});
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
		return state.withProperty(LADDER_FACING, this.getFacing(worldIn, pos))
					.withProperty(LADDER_PROTECTION, this.getLadderProtection(worldIn, pos));
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if(!worldIn.isRemote && worldIn.getBlockState(pos).getBlock() == BlocksRegisterHandler.MULTIBLOCK_LADDER) {
			// Tries to place the ladder protection
			if(!playerIn.isSneaking() && playerIn.getHeldItemMainhand().getItem() == Item.getItemFromBlock(BlocksRegisterHandler.LADDER_PROTECTION) && !this.getLadderProtection(worldIn, pos)) {
				if(!playerIn.isCreative()) playerIn.getHeldItemMainhand().shrink(1);
				this.setLadderProtection(worldIn, pos, true);
				return true;
			}
			// Tries to place a ladder protection on next ladder
			else if (!playerIn.isSneaking() && playerIn.getHeldItemMainhand().getItem() == Item.getItemFromBlock(BlocksRegisterHandler.LADDER_PROTECTION) && this.getLadderProtection(worldIn, pos)) {
				this.placeLadderProtectionOnTop(playerIn, worldIn, pos, 1);
			}
			// Tries to add a ladder on the top
			else if(!playerIn.isSneaking() && playerIn.getHeldItemMainhand().getItem() == Item.getItemFromBlock(BlocksRegisterHandler.MULTIBLOCK_LADDER)) {
				this.placeLadderOnTop(playerIn, worldIn, pos, 1);
				return true;
			}
			// Rotates the block
			else if(!playerIn.isSneaking() && hand == EnumHand.MAIN_HAND && playerIn.getHeldItemMainhand().getItem() == ItemsRegisterHandler.WRENCH) {
				this.rotateBlock(worldIn, pos, EnumFacing.DOWN);
				return true;
			}
			// Tries to remove the ladder protection
			else if(playerIn.isSneaking() && playerIn.getHeldItemMainhand().getItem() == ItemsRegisterHandler.WRENCH && this.getLadderProtection(worldIn, pos)) {
				this.setLadderProtection(worldIn, pos, false);
				EntityItem item = new EntityItem(worldIn, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(BlocksRegisterHandler.LADDER_PROTECTION, 1));
				worldIn.spawnEntity(item);
				return true;
			}
			// Tries to remove the ladder
			else if(playerIn.isSneaking() && playerIn.getHeldItemMainhand().getItem() == ItemsRegisterHandler.WRENCH && !this.getLadderProtection(worldIn, pos)) {
				this.breakBlock(worldIn, pos, state);
				worldIn.setBlockToAir(pos);
				EntityItem item = new EntityItem(worldIn, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(BlocksRegisterHandler.MULTIBLOCK_LADDER, 1));
				worldIn.spawnEntity(item);
				return true;
			}
		}
		
		return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
	}
	
	public EnumFacing getFacing(IBlockAccess world, BlockPos pos) {
		TileEntityMultiblockLadder te = this.getTileEntity(world, pos);
		return te != null ? te.getFacing() : EnumFacing.NORTH;
	}
	
	public boolean getLadderProtection(IBlockAccess world, BlockPos pos) {
		TileEntityMultiblockLadder te = this.getTileEntity(world, pos);
		return te != null ? te.getLadderProtection() : false;
	}
	
	public void setFacing(IBlockAccess world, BlockPos pos, EnumFacing facing) {
		TileEntityMultiblockLadder te = this.getTileEntity(world, pos);
		if(te != null) te.setFacing(facing);
	}
	
	public void setLadderProtection(IBlockAccess world, BlockPos pos, boolean exists) {
		TileEntityMultiblockLadder te = this.getTileEntity(world, pos);
		if(te != null) te.setLadderProtection(exists);
	}
	
	@Override
	public boolean rotateBlock(World world, BlockPos pos, EnumFacing axis) {
		EnumFacing facing = this.getFacing(world, pos);
		this.setFacing(world, pos, facing.rotateAround(axis.getAxis()));
		return true;
	}
	
	public boolean placeLadderOnTop(EntityPlayer playerIn, World worldIn, BlockPos pos, int attempts) {
		// First, tries to place a ladder on the top of the current block
		if(this.canPlaceBlockAt(worldIn, pos.up())) {
			if(!playerIn.isCreative()) playerIn.getHeldItemMainhand().shrink(1);
			
			IBlockState state = this.getDefaultState();
			worldIn.setBlockState(pos.up(), state);
			
			this.setFacing(worldIn, pos.up(), this.getFacing(worldIn, pos));
			
		} 
		// Else, try to propagate the method.
		else if(worldIn.getBlockState(pos.up()).getBlock() == BlocksRegisterHandler.MULTIBLOCK_LADDER && attempts < 8) {
			return this.placeLadderOnTop(playerIn, worldIn, pos.up(), attempts + 1);
		}
		
		return false;
	}
	
	public boolean placeLadderProtectionOnTop(EntityPlayer playerIn, World worldIn, BlockPos pos, int attempts) {
		if(worldIn.getBlockState(pos.up()).getBlock() == BlocksRegisterHandler.MULTIBLOCK_LADDER && attempts < 8) {
			// if already have a ladder protection, tries to place on top block
			if(this.getLadderProtection(worldIn, pos.up())) {
				this.placeLadderProtectionOnTop(playerIn, worldIn, pos.up(), attempts + 1);
			} 
			// else doesnt have a ladder protection place it
			else {
				if(!playerIn.isCreative()) playerIn.getHeldItemMainhand().shrink(1);
				this.setLadderProtection(worldIn, pos.up(), true);
			}
		}
		
		
		
		return false;
	}
	
	@Override
	public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox,
			List<AxisAlignedBB> collidingBoxes, Entity entityIn, boolean p_185477_7_) {
		if(!this.getLadderProtection(worldIn, pos)) { 
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
		} else {
			addCollisionBoxToList(pos, entityBox, collidingBoxes, BlockLadderProtection.AABB_LADDER_PROTECTION_NORTH);
			addCollisionBoxToList(pos, entityBox, collidingBoxes, BlockLadderProtection.AABB_LADDER_PROTECTION_SOUTH);
			addCollisionBoxToList(pos, entityBox, collidingBoxes, BlockLadderProtection.AABB_LADDER_PROTECTION_EAST);
			addCollisionBoxToList(pos, entityBox, collidingBoxes, BlockLadderProtection.AABB_LADDER_PROTECTION_WEST);
		}
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		if(source.getBlockState(pos).getBlock() != BlocksRegisterHandler.MULTIBLOCK_LADDER) {
			return NULL_AABB;
		}
		
		if(!this.getLadderProtection(source, pos)) { 
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
		} else {
			return FULL_BLOCK_AABB;
		}
	}
	
	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		this.releaseItems(worldIn, pos);
		super.breakBlock(worldIn, pos, state);
	}
	
	private void releaseItems(World worldIn, BlockPos pos) {
		if(this.getLadderProtection(worldIn, pos)) {
			EntityItem item = new EntityItem(worldIn, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(BlocksRegisterHandler.LADDER_PROTECTION, 1));
			worldIn.spawnEntity(item);
		}
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
