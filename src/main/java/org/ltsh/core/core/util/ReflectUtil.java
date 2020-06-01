package org.ltsh.core.core.util;

import java.lang.reflect.Field;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 反射工具类
 * @author Ych
 * 2017年5月12日
 */
public class ReflectUtil {
	private static Logger logger = LoggerFactory.getLogger(ReflectUtil.class);
	/**
	 * new　实体
	 * @author Ych
	 * 2017年5月12日
	 * @param objClass
	 * @return
	 */
	public static <T> T createInstance(Class<T> objClass) {
		try {
			return objClass.newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	/**
	 * 给指定对象属性设置参数值
	 * @author Ych
	 * 2017年5月12日
	 * @param bean	对象
	 * @param propertyName	属性名称（区分大小写）支持级联写法，如 dep.user.name
	 * @param propertyValue	属性值
	 */
	
	public static void setFieldValue(Object bean, String propertyName, Object propertyValue) {
		try {
			int dotIndex = propertyName.indexOf(".");
			if(dotIndex != -1) {
				setFieldValue(getFieldValue(bean, propertyName.substring(0, dotIndex)), propertyName.substring(dotIndex + 1), propertyValue);
			}else {
				Field field = bean.getClass().getDeclaredField(propertyName);
				field.setAccessible(true);
				field.set(bean, propertyValue);
			}
		} catch (Exception e) {
			logger.warn("set propertyName failure :propertyName=" + propertyName + ", propertyValue=" + propertyValue);
		}
	}

	/**
	 * 获取指定对象属性设置参数值
	 * @param bean	对象
	 * @param propertyName	属性名称（区分大小写）支持级联写法，如 dep.user.name
	 * @return
	 */
	public static Object getFieldValue(Object bean, String propertyName) {
		Class<?> clazz = bean.getClass();
		Field field = null;
		try {
			int dotIndex = propertyName.indexOf(".");
			if(dotIndex != -1) {
				String subProNameFront = propertyName.substring(0, dotIndex);
				String subProNameBack = propertyName.substring(dotIndex + 1);
				
				Object subBean = getFieldValue(bean, subProNameFront);
				return getFieldValue(subBean, subProNameBack);
			}
			field = clazz.getDeclaredField(propertyName);
		} catch (NoSuchFieldException e) {
		}
		while (clazz != null && field == null) {
			clazz = clazz.getSuperclass();
			try {
				field = clazz.getDeclaredField(propertyName);
			} catch (NoSuchFieldException e2) {
			}
		}
		try {
			if (field != null) {
				field.setAccessible(true);
				return field.get(bean);
			}else {
				logger.warn("get propertyName failure :propertyName=" + propertyName);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}
}
