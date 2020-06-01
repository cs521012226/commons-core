package org.ltsh.core.business.files.excel;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import org.apache.poi.hssf.usermodel.DVConstraint;
import org.apache.poi.hssf.usermodel.HSSFDataValidation;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddressList;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.ltsh.core.business.exp.BusinessException;
import org.ltsh.core.business.files.Export;
import org.ltsh.core.core.util.FileUtil;
import org.ltsh.core.core.util.ReflectUtil;
import org.ltsh.core.core.util.StringUtil;

/**
 * 通用导出excel导出样式
 * @author Ych
 * 2018年11月13日
 */
public abstract class CommonExcelExport<T> implements Export {
	private List<ColInfoVo> colInfoList;
	private String fileName;
	private String fileSuffix = ".xls";
	private String targetDir;
	
	private List<T> data;
	private Workbook wb;
	private Sheet sheet;
	private int rowIndex = 0;	//sheet行下标
	
	private String password;
	private String title;
	
	private String reportDate;
	
	public CommonExcelExport(List<ColInfoVo> colInfoList, List<T> data, String fileName){
		this(colInfoList, data, fileName, null);
	}
	
	public CommonExcelExport(List<ColInfoVo> colInfoList, List<T> data, String fileName, String targetDir){
		this.targetDir = targetDir == null ? targetDir : FileUtil.buildPath(targetDir, false);
		this.data = data;
		this.colInfoList = colInfoList;
		
		int dot = fileName.lastIndexOf(".");
		this.fileName = dot > 0 ? fileName.substring(0, dot) : fileName;
	}
	
	/**
	 * 打开工作簿
	 * @author Ych
	 */
	protected void openWorkbook(){
		try {
			// 建立工作簿
			wb = new HSSFWorkbook();
			// 建立新的sheet对象
			sheet = wb.createSheet(fileName);
			if(!StringUtil.isBlank(password)){
				sheet.protectSheet(password);//设置该Excel为保护
			}
		} catch (Exception e) {
			BusinessException.err(e.getMessage(), e);
		}
	}
	/**
	 * 关闭工作簿
	 * @author Ych
	 */
	protected void closeWorkbook(){
		try {
			if (wb != null) {
				wb.close();
				wb = null;
			}
		} catch (IOException e) {
			BusinessException.err(e.getMessage(), e);
		}
	}
	
	/**
	 * 生成表头
	 * @author YeChao
	 * 2017年6月8日
	 */
	protected void buildHeader(){
		CellStyle headStyle = CellStyleBuilder.instance(wb).setFontBold(true).build();
		
		Row row = null;
		Cell cell = null;
		if(!StringUtil.isBlank(reportDate)){
			row = sheet.createRow(rowIndex++);
			cell = row.createCell(0);
			cell.setCellValue(reportDate);
		}
		
		//合并第一行的所有列
		sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 0, colInfoList.size() - 1));
		
		row = sheet.createRow(rowIndex++);
		row.setHeightInPoints(30);
		//设置第一行大标题
		cell = row.createCell(0);
		cell.setCellValue(title == null ? fileName : title);
		cell.setCellStyle(headStyle);
		
		//设置第二行小标题
		row = sheet.createRow(rowIndex++);
		row.setHeightInPoints(30);
		
//		CellStyle titleStyle = buildTitleStyle();
		CellStyle titleStyle = CellStyleBuilder.instance(wb).setFontBold(true).setCellBackgroudColor(IndexedColors.GREY_25_PERCENT).build();
		for (int j = 0; j < colInfoList.size(); j++) {
			ColInfoVo info = colInfoList.get(j);
			
			//设置单元格宽度
			sheet.setColumnWidth(j, 6000);
			cell = row.createCell(j);
			cell.setCellStyle(titleStyle);
			cell.setCellValue(info.getFieldLabel());
		}
	}
	/**
	 * 生成数据体
	 * @author Ych
	 */
	protected void buildData(){
		if(data == null){
			return ;
		}
		
		CellStyle lockStyle = CellStyleBuilder.instance(wb).setFontSize(11).setCellLock(true).build();
		CellStyle unlockStyle = CellStyleBuilder.instance(wb).setFontSize(11).setCellLock(false).build();
		
		for(T record : data){
			Row row = sheet.createRow(rowIndex++);
			row.setHeightInPoints(30);
			
			for (int j = 0; j < colInfoList.size(); j++) {
				ColInfoVo info = colInfoList.get(j);
				Cell cell = row.createCell(j);
				//是否锁定单元格
				if(info.isLock()){
					cell.setCellStyle(lockStyle);
				}else{
					cell.setCellStyle(unlockStyle);
				}
				//是否下拉框值
				if(info.getDropdown() != null){
					 DVConstraint constraint = DVConstraint.createExplicitListConstraint(info.getDropdown().toArray(new String[]{}));  
			        // 设置数据有效性加载在哪个单元格上,四个参数分别是：起始行、终止行、起始列、终止列  
					CellRangeAddressList  regions = new CellRangeAddressList(row.getRowNum(), row.getRowNum(), j, j);  
			        // 数据有效性对象  
					HSSFDataValidation validate = new HSSFDataValidation(regions, constraint);
					sheet.addValidationData(validate);
				}
				
				Object valueObj = null;
				if(info.getFormat() != null){
					valueObj = info.getFormat().format(record, info, cell);
				}else {
					valueObj = format(record, info, cell);
				}
				String value = valueObj == null ? "" : valueObj.toString();
				cell.setCellValue(value);
			}
		}
	}
	/**
	 * 	获取某条实体属性值
	 * @param record
	 * @param head
	 * @return
	 */
	protected Object format(T record, ColInfoVo colInfoVo, Cell cell) {
		return ReflectUtil.getFieldValue(record, colInfoVo.getFieldName());
	}

	@Override
	public File export() {
		//将文件从内存写到目录中
		File file = null;
		OutputStream output = null;
		if(targetDir == null){
			BusinessException.err("导出存放目录不存在");
		}
		try {
			targetDir = FileUtil.buildPath(targetDir, true);
			file = new File(targetDir + getFileName());
			output = new FileOutputStream(file);
			exportToStream(output);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			FileUtil.close(output);
		}
		return file;
	}

	@Override
	public void exportToStream(OutputStream output) {
		openWorkbook();
		buildHeader();
		buildData();
		
		//将文件从内存写到目录中
		BufferedOutputStream bos = null;
		try {
			bos = new BufferedOutputStream(output, 30 * 1024);
			wb.write(bos);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			FileUtil.close(bos);
			closeWorkbook();
		}
	}

	@Override
	public String getFileName() {
		return fileName + fileSuffix;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getReportDate() {
		return reportDate;
	}
	public void setReportDate(String reportDate) {
		this.reportDate = reportDate;
	}

}
