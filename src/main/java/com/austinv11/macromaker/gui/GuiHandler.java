package com.austinv11.macromaker.gui;

import com.austinv11.macromaker.reference.Reference;
import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class GuiHandler implements IGuiHandler {
	
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		return null;
	}
	
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if (ID == Reference.Guis.WAYPOINT.ordinal())
			return new GuiWaypoint();
		else if (ID == Reference.Guis.MACRO.ordinal())
			return new GuiMacro();
		return null;
	}
}
