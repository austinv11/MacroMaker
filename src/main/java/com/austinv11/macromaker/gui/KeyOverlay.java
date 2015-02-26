package com.austinv11.macromaker.gui;

import com.austinv11.macromaker.reference.Config;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

import java.awt.*;
import java.util.concurrent.ConcurrentLinkedDeque;

public class KeyOverlay extends Gui {
	
	public static final ConcurrentLinkedDeque<Object[]> keys = new ConcurrentLinkedDeque<Object[]>();
	public static final int KEY_LIFE_LENGTH = 90;
	
	@SubscribeEvent
	public void renderOverlay(RenderGameOverlayEvent.Post event) {
		if (Config.showKeysOnHUD) {
			if (/*event.isCanceled() || */event.type != RenderGameOverlayEvent.ElementType.HOTBAR)
				return;
			int x = 1;
			ConcurrentLinkedDeque<Object[]> newKeys = new ConcurrentLinkedDeque<Object[]>();
			while (!keys.isEmpty()) {
				Object[] key = keys.pop();
				String s = (String)key[0];
				int ticksLived = (Integer)key[1];
				Color color = Color.WHITE;
				for (int i = 0; i < KEY_LIFE_LENGTH/(ticksLived*2); i++)
					color = color.darker();
				Minecraft.getMinecraft().fontRenderer.drawString(s, x, 1, color.getRGB());
				x += Minecraft.getMinecraft().fontRenderer.getStringWidth(s)+3;
				if (ticksLived > 1)
					newKeys.add(new Object[]{s, --ticksLived});
			}
			keys.addAll(newKeys);
		}
	}
}
