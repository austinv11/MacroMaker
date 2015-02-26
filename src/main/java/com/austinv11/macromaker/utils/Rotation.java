package com.austinv11.macromaker.utils;

public enum Rotation {
	
	NORTH(0), NORTHEAST(45), EAST(90), SOUTHEAST(135), SOUTH(180), SOUTHWEST(225), WEST(270), NORTHWEST(315);
	
	float yaw;
	
	private Rotation(float yaw) {
		this.yaw = yaw;
	}
	
	public float getYaw() {
		return yaw;
	}
	
	public static Rotation getRotationFromYaw(float yaw) {
		if (0 <= yaw && yaw < 22.5) {
			return NORTH;
		} else if (22.5 <= yaw && yaw < 67.5) {
			return NORTHEAST;
		} else if (67.5 <= yaw && yaw < 112.5) {
			return EAST;
		} else if (112.5 <= yaw && yaw < 157.5) {
			return SOUTHEAST;
		} else if (157.5 <= yaw && yaw < 202.5) {
			return SOUTH;
		} else if (202.5 <= yaw && yaw < 247.5) {
			return SOUTHWEST;
		} else if (247.5 <= yaw && yaw < 292.5) {
			return WEST;
		} else if (292.5 <= yaw && yaw < 337.5) {
			return NORTHWEST;
		} else if (337.5 <= yaw && yaw < 360.0) {
			return NORTH;
		} else {
			return null;
		}
	}
}
