package com.Pirhotau.PiAdditions.Blocks.Decoration.MultiblockGateway;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileEntityMultiblockGateway extends TileEntity {
	
	public EnumFacing facing = EnumFacing.NORTH;

	public EnumFacing getFacing() {
		return this.facing;
	}
	
	public void setFacing(EnumFacing facing) {
		this.facing = facing;
		this.markDirty();
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		nbt.setInteger("facing", this.facing.getHorizontalIndex());
		return super.writeToNBT(nbt);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		this.facing = EnumFacing.getHorizontal(nbt.getInteger("facing"));
		super.readFromNBT(nbt);
	}
	
	private void notifyBlockUpdate() {
		final IBlockState state = getWorld().getBlockState(getPos());
		getWorld().notifyBlockUpdate(getPos(), state, state, 3);
	}
	
	@Override
	public void markDirty() {
		super.markDirty();
		notifyBlockUpdate();
	}
	
	@Override
	public NBTTagCompound getUpdateTag() {
		return writeToNBT(new NBTTagCompound());
	}
	
	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		return new SPacketUpdateTileEntity(this.getPos(), 0, this.getUpdateTag());
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		readFromNBT(pkt.getNbtCompound());
		notifyBlockUpdate();
	}
	
	
	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
		return newState.getBlock() != oldState.getBlock();
	}
}
