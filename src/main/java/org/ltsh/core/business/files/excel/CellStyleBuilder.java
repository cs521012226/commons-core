package org.ltsh.core.business.files.excel;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * CellStyle构建器
 * @author Ych
 * 2019年12月24日
 */
public class CellStyleBuilder {
	
	private Workbook wb;
	private Font font;
	private CellStyle cellStyle;
	
	public static enum Align{
		LEFT, CENTER, RIGHT
	}
	
	public static CellStyleBuilder instance(Workbook wb) {
		return new CellStyleBuilder(wb);
	}
	private CellStyleBuilder() {
		
	}
	
	private CellStyleBuilder(Workbook wb) {
		this.wb = wb;
		initFont();
		initCellStyle();
	}
	
	/**
	 * 初始化默认Font对象
	 * @author Ych
	 * @return
	 */
	private void initFont() {
		font = wb.createFont();
		font.setFontName("宋体");	//设置字体
		setFontSize(11);
		setFontBold(false);
		setFontColor(IndexedColors.BLACK);
	}
	
	/**
	 * 初始化默认CellStyle对象
	 * @author Ych
	 */
	private void initCellStyle() {
		cellStyle = wb.createCellStyle();//单元格样式
		cellStyle.setWrapText(true);//设置自动换行
		setCellLock(false);
		setCellTextAlign(Align.LEFT);	//横向居中
		setCellBorder(true);
		setCellBackgroudColor(IndexedColors.WHITE);
		cellStyle.setFont(font);
	}
	
	/**
	 * 设置字体加粗
	 * @author Ych
	 * @param isBold	True：加粗；False：普通
	 * @return
	 */
	public CellStyleBuilder setFontBold(boolean isBold) {
		if(isBold) {
			font.setBoldweight(Font.BOLDWEIGHT_BOLD);//粗体
		}else {
			font.setBoldweight(Font.BOLDWEIGHT_NORMAL);//正常体
		}
		return this;
	}
	
	/**
	 * 设置字体颜色
	 * @author Ych
	 * @param color
	 * @return
	 */
	public CellStyleBuilder setFontColor(IndexedColors color) {
		font.setColor(color.getIndex());
		return this;
	}
	
	/**
	 * 设置字体大小
	 * @author Ych
	 * @param fontSize
	 * @return
	 */
	public CellStyleBuilder setFontSize(int fontSize) {
		font.setFontHeightInPoints((short) fontSize);//字体大小
		return this;
	}
	
	/**
	 * 设置单元格是否显示边框
	 * @author Ych
	 * @param hasBorder
	 * @return
	 */
	public CellStyleBuilder setCellBorder(boolean hasBorder) {
		short param = 0;
		if(hasBorder) {
			param = CellStyle.BORDER_THIN;
		}else {
			param = CellStyle.BORDER_NONE;
		}
		cellStyle.setBorderBottom(param);
		cellStyle.setBorderLeft(param);
		cellStyle.setBorderRight(param);
		cellStyle.setBorderTop(param);
		return this;
	}

	/**
	 * 设置单元格锁定
	 * @author Ych
	 * @param hasLock	True：锁定；False：不锁定
	 * @return
	 */
	public CellStyleBuilder setCellLock(boolean hasLock) {
		cellStyle.setLocked(hasLock);	//设置是否锁定
		return this;
	}
	
	/**
	 * 设置单元格背景颜色
	 * @author Ych
	 * @param color
	 * @return
	 */
	public CellStyleBuilder setCellBackgroudColor(IndexedColors color) {
		cellStyle.setFillForegroundColor(color.getIndex());		//设置背景颜色
		cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);	//设置背景样式
		return this;
	}
	
	/**
	 * 设置单元格横向居左、居中、居右
	 * @author Ych
	 * @param align	CellStyleBuilder.Align
	 * @return
	 */
	public CellStyleBuilder setCellTextAlign(Align align) {
		switch (align) {
		case LEFT:
			cellStyle.setAlignment(CellStyle.ALIGN_LEFT);	//横向居左
			break;
		case CENTER:
			cellStyle.setAlignment(CellStyle.ALIGN_CENTER);	//横向居中
			break;
		case RIGHT:
			cellStyle.setAlignment(CellStyle.ALIGN_RIGHT);	//横向居右
			break;
		default:
			cellStyle.setAlignment(CellStyle.ALIGN_CENTER);	//横向居中
			break;
		}
		cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);	//纵向居中
		return this;
	}
	
	
	/**
	 * 构建对象
	 * @author Ych
	 * @return
	 */
	public CellStyle build() {
		return cellStyle;
	}
}
