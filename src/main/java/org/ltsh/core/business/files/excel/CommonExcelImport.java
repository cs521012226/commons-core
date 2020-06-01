package org.ltsh.core.business.files.excel;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.ltsh.core.business.exp.BusinessException;
import org.ltsh.core.business.files.Import;
import org.ltsh.core.core.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 导入EXCEL模版父类
 * @author Ych
 * 2018年9月2日
 */
public abstract class CommonExcelImport<T> implements Import<T>{
	protected Logger log = LoggerFactory.getLogger(getClass());
	
	private File file;
	private Workbook wb;
	private Sheet sheet;
	
	public CommonExcelImport(String filePath, String fileName){
		this.file = new File(filePath, fileName);
	}
	public CommonExcelImport(File file){
		this.file = file;
	}
	
	
	protected void openWorkbook(){
		if(!file.exists() || !file.isFile()){
			BusinessException.err("文件" + file.getName() + "不存在");
		}
		// 建立工作簿
		try {
			wb = WorkbookFactory.create(file);
			sheet = wb.getSheetAt(0);//获得第一个sheet
		} catch (Exception e) {
			BusinessException.err(e.getMessage(), e);
		}
	}
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
	 * 读取数据
	 * @author Ych
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<T> readData() {
		openWorkbook();
		List<T> list = new ArrayList<T>();
		try {
			int lastRowNum = sheet.getLastRowNum();	//数据最后行数
			for (int i = 0; i <= lastRowNum; i++) {
				Row row = sheet.getRow(i);
				
				List<String> values = getRowValue(row);
				if(!isValidRow(row, values)){
					continue;
				}
				if(row != null){
					//读取数据
					T vo = newBean(values);
					if(vo == null){
						continue;
					}
					list.add(vo);
				}
			}
		} catch(Exception e) {
			log.error(e.getMessage(), e);
			throw e;
		} finally {
			closeWorkbook();
		}
		return list;
	}
	/**
	 * 创建实例
	 * @author Ych
	 * @return
	 */
	protected abstract T newBean(List<String> values) ;
	
	/**
	 * 	校验是否到了有效数字行
	 * @author Ych
	 * @return
	 */
	protected boolean isValidRow(Row row, List<String> values) {
		int rowNum = row.getRowNum();
		if(rowNum > 0){
			//是否全部数据为空
			for(String v : values){
				if(!StringUtil.isBlank(v)){
					return true;
				}
			}
			return false;
		}
		return false;
	}
	
	
	protected List<String> getRowValue(Row row) {
		List<String> values = new ArrayList<String>();
		for (int i = 0; i < row.getLastCellNum(); i++) {
			String value = getCellData(row.getCell(i));
			values.add(value);
		}
		return values;
	}
	
	/**
	 * 读取单元格值
	 * @author Ych
	 * @param cell
	 * @return
	 */
	protected String getCellData(Cell cell) {
        String cellValue = "";
        if (cell != null) {
            try {
                switch (cell.getCellType()) {
                    case Cell.CELL_TYPE_BLANK://空白
                        cellValue = "";
                        break;
                    case Cell.CELL_TYPE_NUMERIC: //数字、日期  
                    	short format = cell.getCellStyle().getDataFormat();
                        if(DateUtil.isCellDateFormatted(cell)) {
                        	String fmtPattern = "yyyy-MM-dd HH:mm:ss";
                        	if(format == 20 || format == 32) {
                        		fmtPattern = "HH:mm";
                        	}else if (format == 14 || format == 31 || format == 57 || format == 58) {
                        		fmtPattern = "yyyy-MM-dd";
                        	}
                        	SimpleDateFormat fmt = new SimpleDateFormat(fmtPattern);
                            cellValue = fmt.format(cell.getDateCellValue()); //日期型 ,不包含时间 
                        }  
                        else {
                        	cellValue = String.valueOf(cell.getNumericCellValue());
                        }  
                        break; 
                    case Cell.CELL_TYPE_BOOLEAN: //布尔型 4
                        cellValue = String.valueOf(cell.getBooleanCellValue());
                        break;
                    default: 
                    	cellValue = cell.getStringCellValue();
                    	break;
                }
            } catch (Exception e) {
                System.out.println("读取Excel单元格数据出错：" + e.getMessage());
                return cellValue;
            }
        }
        return cellValue;
    }
}
