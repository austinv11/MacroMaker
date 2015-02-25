package com.austinv11.autowalk.event;

import com.austinv11.autowalk.AutoWalk;
import com.austinv11.autowalk.gui.KeyOverlay;
import com.austinv11.autowalk.init.Keybindings;
import com.austinv11.autowalk.reference.Config;
import com.austinv11.autowalk.reference.Reference;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class KeyHandler {
	
	@SubscribeEvent
	public void onKeyInput(InputEvent.KeyInputEvent event) {
		if (Keybindings.openGui.isPressed())
			Minecraft.getMinecraft().thePlayer.openGui(AutoWalk.instance, Reference.Guis.WAYPOINT.ordinal(), Minecraft.getMinecraft().thePlayer.worldObj, (int)Minecraft.getMinecraft().thePlayer.posX, (int)Minecraft.getMinecraft().thePlayer.posY, (int)Minecraft.getMinecraft().thePlayer.posZ);
		else if (Keybindings.macro.isPressed())
			Minecraft.getMinecraft().thePlayer.openGui(AutoWalk.instance, Reference.Guis.MACRO.ordinal(), Minecraft.getMinecraft().thePlayer.worldObj, (int)Minecraft.getMinecraft().thePlayer.posX, (int)Minecraft.getMinecraft().thePlayer.posY, (int)Minecraft.getMinecraft().thePlayer.posZ);
		else if (Keybindings.activateMacros.isPressed())
			TickHandler.isMacroInUse = !TickHandler.isMacroInUse;
		if (Config.showKeysOnHUD) {
			if (Keyboard.getEventKeyState())
				try {
					KeyOverlay.keys.offer(new Object[]{Keyboard.getKeyName(Keyboard.getEventKey()).toUpperCase(), KeyOverlay.KEY_LIFE_LENGTH});
				} catch (Exception e) {
					e.printStackTrace();
				}
		}
	}
	
	@SubscribeEvent
	public void onMouseInput(InputEvent.MouseInputEvent event) {
		if (Config.showKeysOnHUD) {
			if (Mouse.getEventButtonState())
				try {
					KeyOverlay.keys.offer(new Object[]{Mouse.getButtonName(Mouse.getEventButton()).toUpperCase(), KeyOverlay.KEY_LIFE_LENGTH});
				} catch (Exception e) {
					e.printStackTrace();
				}
		}
	}
}
