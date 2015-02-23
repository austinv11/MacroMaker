package com.austinv11.autowalk.event;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.lang.reflect.Field;

public class TickHandler {
	
	public static int X;
	public static int Y;
	public static int Z;
	
	public static boolean isInUse = false;
	
	public Robot robot;
	public boolean isKeyDown = false;
	
	public TickHandler() {
		try {
			robot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}
	
	@SubscribeEvent
	public void onClientTick(TickEvent.ClientTickEvent event) {
		if (isInUse && !isKeyDown) {
			try {
				robot.keyPress(getForwardKey());
			} catch (Exception e) {
				e.printStackTrace();
			}
			isKeyDown = true;
		}
		if (!isInUse && isKeyDown) {
			try {
				robot.keyRelease(getForwardKey());
			} catch (Exception e) {
				e.printStackTrace();
			}
			isKeyDown = false;
		}
	}
	
	public static int getForwardKey() throws NoSuchFieldException, IllegalAccessException {
		GameSettings mc = Minecraft.getMinecraft().gameSettings;
		Field w = mc.getClass().getDeclaredField("keyBindForward");
		String name = Keyboard.getKeyName(((KeyBinding)w.get(mc)).getKeyCode());
		Field key = KeyEvent.class.getField("VK_"+name.toUpperCase());
		return key.getInt(null);
	}
}
