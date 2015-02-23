package com.austinv11.autowalk.init;

import cpw.mods.fml.client.registry.ClientRegistry;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;

public class Keybindings {
	
	public static KeyBinding openGui;
	
	public static void init() {
		openGui = new KeyBinding("key.openRouteGui", Keyboard.KEY_P, "key.categories.misc");
		ClientRegistry.registerKeyBinding(openGui);
	}
}
