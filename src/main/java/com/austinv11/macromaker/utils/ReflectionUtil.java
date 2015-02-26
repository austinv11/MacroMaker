package com.austinv11.macromaker.utils;

import java.lang.reflect.Field;

public class ReflectionUtil {
	
	public static boolean doesClassHaveDeclaredField(Class clazz, String field) {
		Field[] fields = clazz.getDeclaredFields();
		for (Field f : fields)
			if (f.getName().equals(field))
				return true;
		return false;
	}
}
