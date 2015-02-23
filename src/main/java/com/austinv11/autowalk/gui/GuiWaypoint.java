package com.austinv11.autowalk.gui;

import com.austinv11.autowalk.event.TickHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.HashMap;

public class GuiWaypoint extends GuiScreen {
	
	public HashMap<String,GuiTextField> textFields = new HashMap<String,GuiTextField>();
	
	public int textY = height/2 + 3*25;
	public int startTextX = 2;
	
	public int buttonY = height/2 + 4*25;
	public int startButtonX = 2;
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float renderPartialTicks) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		drawBackground(0);
		mc.fontRenderer.drawString("X",  startTextX, textY-10, Color.WHITE.getRGB(), true);
		mc.fontRenderer.drawString("Y",  startTextX+50, textY-10, Color.WHITE.getRGB(), true);
		mc.fontRenderer.drawString("Z",  startTextX+100, textY-10, Color.WHITE.getRGB(), true);
		super.drawScreen(mouseX, mouseY, renderPartialTicks);
		for (GuiTextField text : textFields.values())
			text.drawTextBox();
	}
	
	@Override
	public void initGui() {
		super.initGui();
		textFields.put("x", new GuiTextField(mc.fontRenderer, startTextX, textY, 80, 20));
		textFields.put("y", new GuiTextField(mc.fontRenderer, startTextX+150, textY, 80, 20));
		textFields.put("z", new GuiTextField(mc.fontRenderer, startTextX+300, textY, 80, 20));
		buttonList.add(new GuiButton(0, startButtonX, buttonY, 80, 20, "gui.button.cancel"));
		buttonList.add(new GuiButton(1, startButtonX+50, buttonY, 80, 20, "gui.button.ok"));
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
		for (GuiTextField text : textFields.values())
			text.textboxKeyTyped(eventChar, eventKey);
	}
	
	@Override
	protected void actionPerformed(GuiButton button) {
		if (button.id == 1) {
			TickHandler.isInUse = true;
			TickHandler.X = Integer.parseInt(textFields.get("x").getText());
			TickHandler.Y = Integer.parseInt(textFields.get("y").getText());
			TickHandler.Z = Integer.parseInt(textFields.get("z").getText());
		}
		Minecraft.getMinecraft().thePlayer.closeScreen();
	}
}
