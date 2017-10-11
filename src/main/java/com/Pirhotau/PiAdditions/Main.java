package com.Pirhotau.PiAdditions;

import com.Pirhotau.PiAdditions.Proxy.CommonProxy;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = Main.MODID, name = Main.MODNAME, version = Main.VERSION)
public class Main {

	public static final String MODID = "piadditions";
	public static final String MODNAME = "Pi's Additions";
	public static final String VERSION = "0.1.0";
	
	@Instance
	public static Main instance = new Main();
	
	@SidedProxy(clientSide="com.Pirhotau.PiAdditions.Proxy.ClientProxy", serverSide="com.Pirhotau.PiAdditions.Proxy.ServerProxy")
	public static CommonProxy proxy;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent e) {
		System.out.println("----------------- PreInit ---------------");
	    proxy.preInit(e);
	}
	    
	@EventHandler
	public void init(FMLInitializationEvent e) {
		System.out.println("----------------- Init ---------------");
	    proxy.init(e);        
	}
	    
	@EventHandler
	public void postInit(FMLPostInitializationEvent e) {
		System.out.println("----------------- PostInit ---------------");
		proxy.postInit(e);
	}
}
