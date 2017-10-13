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
	
	public EnumFacing gridFacing = EnumFacing.NORTH;
	public boolean barrierNorth = false;
	public boolean barrierSouth = false;
	public boolean barrierEast = false;
	public boolean barrierWest = false;	

	public EnumFacing getFacing() {
		return this.gridFacing;
	}
	
	public boolean getBarrier(EnumFacing facing) {
		switch(facing) {
		case NORTH: return this.barrierNorth;
		case SOUTH: return this.barrierSouth;
		case EAST: return this.barrierEast;
		case WEST: return this.barrierWest;
		default: return false;
		}
	}
	
	public void setFacing(EnumFacing facing) {
		this.gridFacing = facing;
		this.markDirty();
	}
	
	public void setBarrier(EnumFacing facing, boolean exists) {
		switch(facing) {
		case NORTH: {
			this.barrierNorth = exists;
			break;
		}
		case SOUTH: {
			this.barrierSouth = exists;
			break;
		}
		case EAST: {
			this.barrierEast = exists;
			break;
		}
		case WEST: {
			this.barrierWest = exists;
			break;
		}
		default: break;
		}
		this.markDirty();
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		nbt.setInteger("gridFacing", this.gridFacing.getHorizontalIndex());
		nbt.setBoolean("barrierNorth", this.barrierNorth);
		nbt.setBoolean("barrierSouth", this.barrierSouth);
		nbt.setBoolean("barrierEast", this.barrierEast);
		nbt.setBoolean("barrierWest", this.barrierWest);
		return super.writeToNBT(nbt);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		this.gridFacing = EnumFacing.getHorizontal(nbt.getInteger("gridFacing"));
		this.barrierNorth = nbt.getBoolean("barrierNorth");
		this.barrierSouth = nbt.getBoolean("barrierSouth");
		this.barrierEast = nbt.getBoolean("barrierEast");
		this.barrierWest = nbt.getBoolean("barrierWest");
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
