package com.Pirhotau.PiAdditions.Blocks.Decoration.MultiblockGateway;

import javax.annotation.Nullable;

import com.Pirhotau.PiAdditions.Blocks.PBlockTileEntity;
import com.Pirhotau.PiAdditions.Blocks.Decoration.Grid.TileEntityGrid;
import com.mojang.realmsclient.dto.WorldTemplate.WorldTemplateType;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
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
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockMultiblockGateway extends PBlockTileEntity<TileEntityMultiblockGateway> {
	
	public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);

	public BlockMultiblockGateway() {
		super("multiblock_gateway");
		this.setDefaultState(this.getDefaultState()
				.withProperty(FACING, EnumFacing.NORTH)
			);
	}

	/*
	 * ------------- ORIENTATION
	 */
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] {FACING});
	}
	
	
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		worldIn.setBlockState(pos, state.withProperty(FACING, placer.getHorizontalFacing().getOpposite()));
		TileEntityMultiblockGateway te = this.getTileEntity(worldIn, pos);
		te.setFacing(placer.getHorizontalFacing().getOpposite());
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
		return 0;
	}
	
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		return state.withProperty(FACING, this.getFacing(worldIn, pos));
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if(!worldIn.isRemote) {
			if(playerIn.isSneaking() && hand == EnumHand.MAIN_HAND) {
				return this.rotateBlock(worldIn, pos, facing);
			} else return false;
		}
		return false;
	}
	
	public EnumFacing getFacing(IBlockAccess world, BlockPos pos) {
		TileEntityMultiblockGateway te = this.getTileEntity(world, pos);
		return te != null ? te.getFacing() : EnumFacing.NORTH;
	}
	
	public void setFacing(IBlockAccess world, BlockPos pos, EnumFacing facing) {
		TileEntityMultiblockGateway te = this.getTileEntity(world, pos);
		if(te != null) te.setFacing(facing);
	}
	
	@Override
	public boolean rotateBlock(World world, BlockPos pos, EnumFacing axis) {
		EnumFacing facing = this.getFacing(world, pos);
		this.setFacing(world, pos, facing.rotateAround(axis.getAxis()));
		return true;
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
