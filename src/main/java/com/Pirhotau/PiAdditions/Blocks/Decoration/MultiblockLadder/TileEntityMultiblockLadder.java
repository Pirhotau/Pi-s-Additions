package com.Pirhotau.PiAdditions.Blocks.Decoration.MultiblockLadder;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileEntityMultiblockLadder extends TileEntity {
	public EnumFacing ladderFacing = EnumFacing.NORTH;
	public boolean ladderProtection = false;
	
	public EnumFacing getFacing() {
		return this.ladderFacing;
	}
	
	public boolean getLadderProtection() {
		return this.ladderProtection;
	}
	
	public void setFacing(EnumFacing facing) {
		this.ladderFacing = facing;
		this.markDirty();
	}
	
	public void setLadderProtection(boolean exists) {
		this.ladderProtection = exists;
		this.markDirty();
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		nbt.setInteger("ladderFacing", this.ladderFacing.getHorizontalIndex());
		nbt.setBoolean("ladderProtection", this.ladderProtection);
		return super.writeToNBT(nbt);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		this.ladderFacing = EnumFacing.getHorizontal(nbt.getInteger("ladderFacing"));
		this.ladderProtection = nbt.getBoolean("ladderProtection");
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
