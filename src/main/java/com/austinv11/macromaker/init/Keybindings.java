package com.austinv11.macromaker.init;

import cpw.mods.fml.client.registry.ClientRegistry;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;

public class Keybindings {
	
	public static KeyBinding openGui;
	public static KeyBinding macro;
	public static KeyBinding seperator;
	public static KeyBinding activateMacros;
	
	public static void init() {
		openGui = new KeyBinding("key.openRouteGui", Keyboard.KEY_P, "key.categories.macromaker");
		ClientRegistry.registerKeyBinding(openGui);
		macro = new KeyBinding("key.macro", Keyboard.KEY_M, "key.categories.macromaker");
		ClientRegistry.registerKeyBinding(macro);
		seperator = new KeyBinding("key.seperator", Keyboard.KEY_COMMA, "key.categories.macromaker");
		ClientRegistry.registerKeyBinding(seperator);
		activateMacros = new KeyBinding("key.activateMacros", Keyboard.KEY_L, "key.categories.macromaker");
		ClientRegistry.registerKeyBinding(activateMacros);
	}
}
