package com.austinv11.autowalk.utils;

import java.util.List;

public class Utils {
	
	public static Integer[] getIntArrayFromList(List<Integer> list) {
		Integer[] array = new Integer[list.size()];
		for (int i = 0; i < list.size(); i++)
			array[i] = list.get(i);
		return array;
	}
}
