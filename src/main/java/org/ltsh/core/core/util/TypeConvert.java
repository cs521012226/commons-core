package org.ltsh.core.core.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Java类型转换
 * @author Ych
 * 2017年5月12日
 */
public class TypeConvert {
	/**
	 * 类型转换，目前只支持String转Number、Number转String，其余的不作转换直接返回src对象
	 * @author Ych
	 * @param src	需要转换的对象
	 * @param clazz	转换后期望的类型
	 * @return		转换后对象
	 */
	public static Object convertType(Object src, Class<?> clazz){
		
		if(src instanceof String){
			String data = (String) src;
			
			if(int.class == clazz || Integer.class == clazz){
				try{
					return Integer.valueOf(data);
				}catch(NumberFormatException e){
					return 0;
				}
			}else if(long.class == clazz || Long.class == clazz){
				try{
					return Long.valueOf(data);
				}catch(NumberFormatException e){
					return 0L;
				}
			}else if(double.class == clazz || Double.class == clazz){
				try{
					return Double.valueOf(data);
				}catch(NumberFormatException e){
					return 0.0;
				}
			}else if(BigDecimal.class == clazz){
				if(StringUtil.isBlank(data)){
					data = "0.00";
				}
				return new BigDecimal(data);
			}else if(java.sql.Date.class == clazz || java.util.Date.class == clazz){
				long time = 0L;
				try {
					time = Long.valueOf(data);
					if(time == 0){
						return null;
					}
				} catch (NumberFormatException e) {
					return null;
				}
				return new java.util.Date(time);
			}else if(Boolean.class == clazz || boolean.class == clazz){
				return Boolean.valueOf(data);
			}
		}else if(src instanceof String[]){
			if(String.class == clazz){
				return src.toString();
			}
		}else if(src instanceof Number){
			Number data = (Number) src;
			if(String.class == clazz){
				return data.toString();
			}else if(int.class == clazz || Integer.class == clazz){
				return data.intValue();
			}else if(long.class == clazz || Long.class == clazz){
				return data.longValue();
			}else if(float.class == clazz || Float.class == clazz){
				return data.floatValue();
			}else if(double.class == clazz || Double.class == clazz){
				return data.doubleValue();
			}else if(java.sql.Date.class == clazz || java.util.Date.class == clazz){
				return new java.util.Date(data.longValue());
			}
		}
		return src;
	}
	
	/**
	 * 数组转List
	 * @author Ych
	 * @param arr
	 * @param impl
	 * @return
	 */
	public static <T> List<T> toList(T[] arr, List<T> impl){
		for(T d : arr){
			impl.add(d);
		}
		return impl;
	}
	/**
	 * 数组转List
	 * @author Ych
	 * @param arr
	 * @return
	 */
	public static <T> List<T> toList(T[] arr){
		return toList(arr, new ArrayList<T>());
	}
	
	/**
	 * Map转List
	 * @author Ych
	 * @param map
	 * @param impl
	 * @return
	 */
	public static <T> List<T> toList(Map<?, T> map, List<T> impl){
		for(Map.Entry<?, T> m : map.entrySet()){
			impl.add(m.getValue());
		}
		return impl;
	}
	/**
	 * Map转List
	 * @author Ych
	 * @param map
	 * @return
	 */
	public static <T> List<T> toList(Map<?, T> map){
		return toList(map, new ArrayList<T>());
	}
	
	/**
	 * Object转Long
	 * @author Ych
	 * @param value
	 * @return
	 */
	public static Long toLong(Object value){
		if(value instanceof Number){
			return ((Number) value).longValue();
		}else if(value instanceof String){
			return Long.valueOf((String) value);
		}
		return (Long) value;
	}
	
	/**
	 * Object转Integer
	 * @author Ych
	 * @param value
	 * @return
	 */
	public static Integer toInt(Object value){
		if(value instanceof Number){
			return ((Number) value).intValue();
		}else if(value instanceof String){
			return Integer.valueOf((String) value);
		}
		return (Integer) value;
	}
}
