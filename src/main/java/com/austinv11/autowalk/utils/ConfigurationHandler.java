package com.austinv11.autowalk.utils;

import com.austinv11.autowalk.reference.Config;
import com.austinv11.autowalk.reference.Reference;
import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.common.config.Configuration;

import java.io.File;

public class ConfigurationHandler {
	
	public static Configuration config;
	
	public static void init(File configFile){
		if (config == null) {
			config = new Configuration(configFile);
		}
		loadConfiguration();
	}
	
	@SubscribeEvent
	public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
		if (event.modID.equalsIgnoreCase(Reference.MOD_ID)) {
			loadConfiguration();
		}
	}
	
	private static void loadConfiguration(){
		try{//Load & read properties
			config.load();
			Config.showKeysOnHUD = config.get("Misc", "showKeysOnHUD", false, "If enabled, all pressed keys will be overlaid on the in game HUD").getBoolean(false);
			Config.listenOnMacroScreen = config.get("Misc", "listenOnMacroScreen", true, "When enabled, keys will be listened for on the macro configuration screen").getBoolean(true);
			Config.useMacrosOnAllScreens = config.get("Misc", "useMacrosOnAllScreens", false, "Enabling this allows macros to be used within guis. WARNING: Enabling this may cause some weird effects").getBoolean(false);
			Config.useTransparentBackground = config.get("Misc", "useTransparentBackground", true, "Disabling makes the guis use the default Minecraft dirt background").getBoolean(true);
		}catch (Exception e){//Log exception
			Logger.warn("Config exception!");
			Logger.warn(e.getStackTrace());
		}finally {//Save
			if (config.hasChanged()) {
				config.save();
			}
		}
	}
}
