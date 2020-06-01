package org.ltsh.core.business.files.excel;

import org.apache.poi.ss.usermodel.Cell;

public interface CellFormat<T> {

	/**
	 * 记录格式化
	 * @author Ych
	 * @param value
	 * @param record
	 * @return 返回自定义格式化后的值
	 */
	public Object format(T record, ColInfoVo colInfo, Cell cell) ;
}
