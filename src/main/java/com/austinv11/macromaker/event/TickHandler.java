package com.austinv11.macromaker.event;

import com.austinv11.macromaker.init.Keybindings;
import com.austinv11.macromaker.reference.Config;
import com.austinv11.macromaker.utils.ReflectionUtil;
import com.austinv11.macromaker.utils.Rotation;
import com.austinv11.macromaker.utils.Utils;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentTranslation;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.lang.reflect.Field;
import java.util.ArrayList;

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
		if (Minecraft.getMinecraft().currentScreen == null || Config.useMacrosOnAllScreens) {
			if (isMacroInUse && !wasMacroUsed) {
				try {
					if (macros != null) {
						Integer[][] pressed = parseMacros();
						Integer[] keys = pressed[0];
						Integer[] mouse = pressed[1];
						for (int i = 0; i < keys.length; i++) {
							robot.keyPress(keys[i]);
						}
						for (int i = 0; i < mouse.length; i++) {
							robot.mousePress(InputEvent.getMaskForButton(mouse[i]));
						}
						wasMacroUsed = true;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (!isMacroInUse && wasMacroUsed) {
				try {
					Integer[][] pressed = parseMacros();
					Integer[] keys = pressed[0];
					Integer[] mouse = pressed[1];
					for (int i = 0; i < keys.length; i++) {
						robot.keyRelease(keys[i]);
					}
					for (int i = 0; i < mouse.length; i++) {
						robot.mouseRelease(InputEvent.getMaskForButton(mouse[i]));
					}
					wasMacroUsed = false;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} else if (isMacroInUse || wasMacroUsed) {
			if (macros != null) {
				isMacroInUse = false;
				wasMacroUsed = false;
				try {
					Integer[][] pressed = parseMacros();
					Integer[] keys = pressed[0];
					Integer[] mouse = pressed[1];
					for (int i = 0; i < keys.length; i++) {
						robot.keyRelease(keys[i]);
					}
					for (int i = 0; i < mouse.length; i++) {
						robot.mouseRelease(InputEvent.getMaskForButton(mouse[i]));
					}
					wasMacroUsed = false;
				} catch (Exception e) {
					e.printStackTrace();
				}
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
		String characterUp = character.toUpperCase();
		if (characterUp.contains("SHIFT"))
			return KeyEvent.VK_SHIFT;
		else if (characterUp.contains("META"))
			return KeyEvent.VK_META;
		if (ReflectionUtil.doesClassHaveDeclaredField(KeyEvent.class, "VK_"+character.toUpperCase())) {
			Field key = KeyEvent.class.getField("VK_"+character.toUpperCase());
			return key.getInt(null);
		} else {//Special cases
			return 0;
		}
	}
	
	public static int getMouse(String button) {
		String num = button.toUpperCase().replace("BUTTON", "");
		switch (Integer.valueOf(num)) {
			case 0:
				return 1;
			case 1:
				return 3;
			case 2:
				return 2;
		}
		return 0;
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
	
	private Integer[][] parseMacros() throws NoSuchFieldException, IllegalAccessException {
		String[] split = macros.toLowerCase().split(String.valueOf((char)getKey(Keyboard.getKeyName(Keybindings.seperator.getKeyCode()))));
		ArrayList<Integer> keys = new ArrayList<Integer>();
		ArrayList<Integer> mouse = new ArrayList<Integer>();
		for (int i = 0; i < split.length; i++)
			if (!split[i].toUpperCase().contains("BUTTON"))
				keys.add(getKey(split[i]));
			else
				mouse.add(getMouse(split[i]));
		return new Integer[][]{Utils.getIntArrayFromList(keys), Utils.getIntArrayFromList(mouse)};
	}
}
