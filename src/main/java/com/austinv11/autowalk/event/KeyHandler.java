package com.austinv11.autowalk.event;

import com.austinv11.autowalk.AutoWalk;
import com.austinv11.autowalk.init.Keybindings;
import com.austinv11.autowalk.reference.Reference;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;
import net.minecraft.client.Minecraft;

public class KeyHandler {
	
	@SubscribeEvent
	public void onKeyInput(InputEvent.KeyInputEvent event) {
		if (Keybindings.openGui.isPressed())
			Minecraft.getMinecraft().thePlayer.openGui(AutoWalk.instance, Reference.Guis.WAYPOINT.ordinal(), Minecraft.getMinecraft().thePlayer.worldObj, (int)Minecraft.getMinecraft().thePlayer.posX, (int)Minecraft.getMinecraft().thePlayer.posY, (int)Minecraft.getMinecraft().thePlayer.posZ);
		else if (Keybindings.macro.isPressed())
			Minecraft.getMinecraft().thePlayer.openGui(AutoWalk.instance, Reference.Guis.MACRO.ordinal(), Minecraft.getMinecraft().thePlayer.worldObj, (int)Minecraft.getMinecraft().thePlayer.posX, (int)Minecraft.getMinecraft().thePlayer.posY, (int)Minecraft.getMinecraft().thePlayer.posZ);
		else if (Keybindings.activateMacros.isPressed())
			TickHandler.isMacroInUse = !TickHandler.isMacroInUse;
	}
}
