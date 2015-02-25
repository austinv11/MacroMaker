package com.austinv11.autowalk.init;

import cpw.mods.fml.client.registry.ClientRegistry;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;

public class Keybindings {
	
	public static KeyBinding openGui;
	public static KeyBinding macro;
	public static KeyBinding seperator;
	public static KeyBinding activateMacros;
	
	public static void init() {
		openGui = new KeyBinding("key.openRouteGui", Keyboard.KEY_P, "key.categories.autowalk");
		ClientRegistry.registerKeyBinding(openGui);
		macro = new KeyBinding("key.macro", Keyboard.KEY_M, "key.categories.autowalk");
		ClientRegistry.registerKeyBinding(macro);
		seperator = new KeyBinding("key.seperator", Keyboard.KEY_COMMA, "key.categories.autowalk");
		ClientRegistry.registerKeyBinding(seperator);
		activateMacros = new KeyBinding("key.activateMacros", Keyboard.KEY_L, "key.categories.autowalk");
		ClientRegistry.registerKeyBinding(activateMacros);
	}
}
