package com.austinv11.autowalk.gui;

import com.austinv11.autowalk.event.TickHandler;
import com.austinv11.autowalk.init.Keybindings;
import com.austinv11.autowalk.reference.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.StatCollector;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.*;

public class GuiMacro extends GuiScreen {
	
	public HashMap<String,GuiTextField> textFields = new HashMap<String,GuiTextField>();
	
	public int textY = height/2 + 3*25;
	public int startTextX = (int) (width/2 + 2*25);
	
	public int buttonY = height/2 + 5*25;
	public int startButtonX = (int) (width/2 + 4.5*25);
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float renderPartialTicks) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		drawBackground(0);
		mc.fontRenderer.drawString(StatCollector.translateToLocal("gui.macro").replace("@REPLACE@", Keyboard.getKeyName(Keybindings.seperator.getKeyCode()).toLowerCase()),  startTextX, textY-10, Color.WHITE.getRGB(), true);
		super.drawScreen(mouseX, mouseY, renderPartialTicks);
		for (GuiTextField text : textFields.values())
			text.drawTextBox();
	}
	
	@Override
	public void initGui() {
		super.initGui();
		textFields.put("macros", getTextbox(startTextX, textY, 320, 20, TickHandler.macros));
		buttonList.add(new GuiButton(0, startButtonX, buttonY, 80, 20, StatCollector.translateToLocal("gui.button.cancel")));
		buttonList.add(new GuiButton(1, startButtonX+100, buttonY, 80, 20, StatCollector.translateToLocal("gui.button.ok")));
	}
	
	private GuiTextField getTextbox(int x, int y, int height, int width, String startText) {
		GuiTextField field = new GuiTextField(mc.fontRenderer, x, y, height, width);
		if (startText != null)
			field.setText(startText);
		return field;
	}
	
	@Override
	public boolean doesGuiPauseGame() {
		return true;
	}
	
	@Override
	protected void mouseClicked(int x, int y, int mouseEvent) {
		super.mouseClicked(x, y, mouseEvent);
		for (GuiTextField text : textFields.values())
			text.mouseClicked(x, y, mouseEvent);
	}
	
	@Override
	public void updateScreen() {
		super.updateScreen();
		for (GuiTextField text : textFields.values())
			text.updateCursorCounter();
	}
	
	@Override
	protected void keyTyped(char eventChar, int eventKey) {
		super.keyTyped(eventChar, eventKey);
		boolean cont = false;
		for (GuiTextField text : textFields.values()) {
			text.textboxKeyTyped(eventChar, eventKey);
			cont = text.isFocused() || cont;
		}
		if (cont)
			if (Config.listenOnMacroScreen)
				if (Keyboard.getEventKeyState()) {
					String currentText = textFields.get("macros").getText();
					currentText = (currentText != null && currentText.length() >= 1 ? currentText+"," : "")+Keyboard.getKeyName(Keyboard.getEventKey());
					textFields.get("macros").setText(eliminateRepeats(currentText));
				}
	}
	
	@Override
	protected void actionPerformed(GuiButton button) {
		if (button.id == 1) {
			TickHandler.macros = textFields.get("macros").getText();
		}
		Minecraft.getMinecraft().thePlayer.closeScreen();
	}
	
	public static String eliminateRepeats(String text) {
		try {
			String seperator = String.valueOf((char) TickHandler.getKey(Keyboard.getKeyName(Keybindings.seperator.getKeyCode())));
			if (!text.contains(seperator))
				return text;
			String[] split = text.split(seperator);
			java.util.List<String> splitList = new ArrayList<String>();
			for (String s : split)
				if (!splitList.contains(s.toUpperCase()))
					splitList.add(s.toUpperCase());
			String newString = null;
			for (String s2 : splitList) {
				if (newString == null)
					newString = s2;
				else
					newString = newString+seperator+s2;
			}
			return newString;
		}catch (Exception e) {
			e.printStackTrace();
		}
		return text;
	}
}
