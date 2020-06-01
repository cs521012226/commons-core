package org.ltsh.core.core.vo;

import java.math.BigDecimal;

import org.ltsh.core.core.util.NumberUtil;

/**
 * 文件大小单位
 * @author Ych
 * 2018年7月26日
 */
public enum FileUnit {

	BIT(1, 8, "bit"),
	BYTE(2, 1024, "B"),
	KB(3, 1024, "KB"),
	MB(4, 1024, "MB"),
	GB(5, 1024, "GB"),
	TB(6, 1024, "TB"),
	PB(7, 1024, "PB");
	
	private int sizeOrder;
	private int scale;
	private String unitName;
	
	FileUnit(int sizeOrder, int scale, String unitName){
		this.sizeOrder = sizeOrder;
		this.scale = scale;
		this.unitName = unitName;
	}
	/**
	 * 通过大小序列获取实例
	 * @author Ych
	 * @param sizeOrder
	 * @return
	 */
	private FileUnit getBySizeOrder(int sizeOrder){
		for(FileUnit u : values()){
			if(u.sizeOrder == sizeOrder){
				return u;
			}
		}
		return null;
	}
	
	/**
	 * 到某个单位的跨度
	 * @author Ych
	 * @return
	 */
	public long space(FileUnit target){
		if(this == target){
			return 0L;
		}
		int index = 0;
		int limit = 0;
		if (sizeOrder < target.sizeOrder) {
			index = sizeOrder;
			limit = target.sizeOrder;
		} else if (sizeOrder > target.sizeOrder){
			index = target.sizeOrder;
			limit = sizeOrder;
		}
		long sum = 1L;
		for (int i = index + 1; i <= limit; i++) {
			FileUnit unit = getBySizeOrder(i);
			sum *= unit.scale;
		}
		return sum;
	}
	
	/**
	 * 计算文件大小尺寸
	 * @author Ych
	 * @param srcSize	原大小
	 * @param toSize	转到指定单位大小
	 * @return
	 */
	public BigDecimal calSize(double srcSize, FileUnit target){
		BigDecimal sum = new BigDecimal(srcSize);
		if(this.equals(target)){	//同一个单位不作转换
			return new BigDecimal(srcSize);
		}
		if (this.sizeOrder < target.sizeOrder) { // 从小单位转大单位
			sum = sum.divide(new BigDecimal(this.space(target)));
		} else { // 从大单位转小单位
			sum = sum.multiply(new BigDecimal(this.space(target)));
		}
		return sum;
	}
	
	/**
	 * 技术某字节大小自动转为最接近单位的文件大小（带单位）
	 * @author Ych
	 * @param byteSize
	 * @return
	 */
	public static String size(long byteSize){
		BigDecimal rs = null;
		String unitStr = null;
    	if(byteSize < BYTE.scale) {
    		rs = BYTE.calSize(byteSize, BYTE);
    		unitStr = BYTE.unitName;
    	} else if(byteSize < BYTE.space(MB)) {
    		rs = BYTE.calSize(byteSize, KB);
    		unitStr = KB.unitName;
    	} else if(byteSize < BYTE.space(GB)) {
    		rs = BYTE.calSize(byteSize, MB);
    		unitStr = MB.unitName;
    	} else if(byteSize < BYTE.space(TB)) {
    		rs = BYTE.calSize(byteSize, GB);
    		unitStr = GB.unitName;
    	} else if(byteSize < BYTE.space(PB)) {
    		rs = BYTE.calSize(byteSize, TB);
    		unitStr = TB.unitName;
    	} 
    	return NumberUtil.format(rs) + unitStr;
	}
}
