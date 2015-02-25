package com.austinv11.autowalk.event;

import com.austinv11.autowalk.init.Keybindings;
import com.austinv11.autowalk.utils.Rotation;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentTranslation;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.lang.reflect.Field;

public class TickHandler {
	
	public static int X;
	public static int Y;
	public static int Z;
	
	public static boolean isPathfinderInUse = false;
	public static boolean isMacroInUse = false;
	
	private boolean wasMacroUsed = false;
	
	public static String macros;
	
	public Robot robot;
	public boolean isKeyDown = false;
	
	private boolean isSpaceDown = false;
	
	private int inactivityTicker = 0;
	private int cooldown = 0;
	
	private static boolean pathfindReset = false;
	
	public TickHandler() {
		try {
			robot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}
	
	@SubscribeEvent
	public void onClientTick(TickEvent.ClientTickEvent event) {
		if (isMacroInUse && !wasMacroUsed) {
			try {
				int[] keys = parseMacros();
				for (int i = 0; i < keys.length; i++) {
					robot.keyPress(keys[i]);
				}
				wasMacroUsed = true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (!isMacroInUse && wasMacroUsed) {
			try {
				int[] keys = parseMacros();
				for (int i = 0; i < keys.length; i++) {
					robot.keyRelease(keys[i]);
				}
				wasMacroUsed = false;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (pathfindReset) {
			try {
				isPathfinderInUse = false;
				isKeyDown = false;
				isSpaceDown = false;
				inactivityTicker = 0;
				robot.keyRelease(getForwardKey());
				robot.keyRelease(getJumpKey());
				pathfindReset = false;
			}catch (Exception e) {}
		}
		if (isPathfinderInUse && inactivityTicker == 0 && cooldown == 0)
			setYaw();
		if (isPathfinderInUse && !isKeyDown) {
			try {
				robot.keyPress(getForwardKey());
			} catch (Exception e) {
				e.printStackTrace();
			}
			isKeyDown = true;
		}
		if (!isPathfinderInUse && isKeyDown) {
			try {
				robot.keyRelease(getForwardKey());
			} catch (Exception e) {
				e.printStackTrace();
			}
			isKeyDown = false;
		}
		if (isPathfinderInUse) {
			EntityPlayer player = Minecraft.getMinecraft().thePlayer;
			if (Minecraft.getMinecraft().currentScreen != null) {
				markForPathfindReset();
				return;
			}
			if (player != null) {
				if (Math.abs(player.posX-X) < 1 && Math.abs(player.posZ-Z) < 1) {
					markForPathfindReset();
					player.addChatComponentMessage(new ChatComponentTranslation("chat.arrived"));
					return;
				}
//				Logger.info(Math.abs(player.lastTickPosX-player.posX)+", "+Math.abs(player.lastTickPosZ-player.posZ));
				if (Math.abs(player.lastTickPosX-player.posX) <= .01 && Math.abs(player.lastTickPosZ-player.posZ) <= .01 || ((Math.abs(player.lastTickPosX-player.posX) <= .1 && Math.abs(player.lastTickPosZ-player.posZ) <= .1) && inactivityTicker > 10)) {
					if (inactivityTicker > 90) {
						markForPathfindReset();
						player.addChatComponentMessage(new ChatComponentTranslation("chat.failed"));
						return;
					}
					if (inactivityTicker > 3) {
						if (!isSpaceDown) {
							try {
								robot.keyPress(getJumpKey());
							} catch (Exception e) {
								e.printStackTrace();
							}
							isSpaceDown = true;
						} else {
							try {
								robot.keyRelease(getJumpKey());
							} catch (Exception e) {
								e.printStackTrace();
							}
							isSpaceDown = false;
						}
					}
					if (inactivityTicker >= 10 && inactivityTicker%10 == 0) {
						float newYaw = player.rotationYaw+45;
						Minecraft.getMinecraft().thePlayer.rotationYaw = newYaw > 360 ? newYaw-360 : newYaw;
					}
					inactivityTicker++;
				} else {
					if (inactivityTicker > 0)
						cooldown = 70;
					inactivityTicker = 0;
				}
			}
		}
		if (cooldown > 0)
			cooldown--;
	}
	
	public static int getForwardKey() throws NoSuchFieldException, IllegalAccessException {
		GameSettings mc = Minecraft.getMinecraft().gameSettings;
		Field w = mc.getClass().getDeclaredField("keyBindForward");
		String name = Keyboard.getKeyName(((KeyBinding)w.get(mc)).getKeyCode());
		Field key = KeyEvent.class.getField("VK_"+name.toUpperCase());
		return key.getInt(null);
	}
	
	public static int getJumpKey() throws NoSuchFieldException, IllegalAccessException {
		GameSettings mc = Minecraft.getMinecraft().gameSettings;
		Field w = mc.getClass().getDeclaredField("keyBindJump");
		String name = Keyboard.getKeyName(((KeyBinding)w.get(mc)).getKeyCode());
		Field key = KeyEvent.class.getField("VK_"+name.toUpperCase());
		return key.getInt(null);
	}
	
	public static int getKey(String character) throws IllegalAccessException, NoSuchFieldException {
		Field key = KeyEvent.class.getField("VK_"+character.toUpperCase());
		return key.getInt(null);
	}
	
	private void setYaw() {
		float yaw = 0;
		int x = (int) Minecraft.getMinecraft().thePlayer.posX;
		int z = (int) Minecraft.getMinecraft().thePlayer.posZ;
		if (z < Z && x == X)
			yaw = Rotation.NORTH.getYaw();
		else if (z < Z && x > X)
			yaw = Rotation.NORTHEAST.getYaw();
		else if (z == Z && x > X)
			yaw = Rotation.EAST.getYaw();
		else if (z > Z && x > X)
			yaw = Rotation.SOUTHEAST.getYaw();
		else if (z > Z && x == X)
			yaw = Rotation.SOUTH.getYaw();
		else if (z > Z && x < X)
			yaw = Rotation.SOUTHWEST.getYaw();
		else if (z == Z && x < X)
			yaw = Rotation.WEST.getYaw();
		else if (z < Z && x < X)
			yaw = Rotation.NORTHWEST.getYaw();
		Minecraft.getMinecraft().thePlayer.rotationYaw = yaw;
	}
	
	public static void markForPathfindReset() {
		pathfindReset = true;
	}
	
	private int[] parseMacros() throws NoSuchFieldException, IllegalAccessException {
		String[] split = macros.toLowerCase().split(String.valueOf((char)getKey(Keyboard.getKeyName(Keybindings.seperator.getKeyCode()))));
		int[] keys = new int[split.length];
		for (int i = 0; i < split.length; i++)
			keys[i] = getKey(split[i]);
		return keys;
	}
}
