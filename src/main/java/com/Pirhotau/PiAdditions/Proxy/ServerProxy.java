package com.Pirhotau.PiAdditions.Proxy;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.SERVER)
public class ServerProxy extends CommonProxy {
	
	public void preInit(FMLPreInitializationEvent e) {
	    super.preInit(e);
	}
	    
	
	public void init(FMLInitializationEvent e) {
		super.init(e);
	}
	    
	
	public void postInit(FMLPostInitializationEvent e) {
		super.postInit(e);
	}
}
