package com.austinv11.autowalk;

import com.austinv11.autowalk.event.KeyHandler;
import com.austinv11.autowalk.event.TickHandler;
import com.austinv11.autowalk.gui.GuiHandler;
import com.austinv11.autowalk.gui.KeyOverlay;
import com.austinv11.autowalk.init.Keybindings;
import com.austinv11.autowalk.reference.Reference;
import com.austinv11.autowalk.utils.ConfigurationHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import net.minecraftforge.common.MinecraftForge;

@Mod(modid= Reference.MOD_ID,name = Reference.MOD_NAME,version = Reference.VERSION/*, guiFactory = Reference.GUI_FACTORY_CLASS*/)
public class AutoWalk {
	
	@Mod.Instance(Reference.MOD_ID)
	public static AutoWalk instance;
	
	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		ConfigurationHandler.init(event.getSuggestedConfigurationFile());
		Keybindings.init();
		FMLCommonHandler.instance().bus().register(new KeyHandler());
		FMLCommonHandler.instance().bus().register(new TickHandler());
		MinecraftForge.EVENT_BUS.register(new KeyOverlay());
	}
	
	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {
		NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GuiHandler());
	}
	
	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		
	}
}
