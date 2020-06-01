package org.ltsh.core.business.files.excel;

import java.util.List;

import org.ltsh.core.business.files.excel.CellFormat;

import com.alibaba.fastjson.JSONObject;

/**
 * 单元格消息对象
 * @author Ych
 * 2018年8月21日
 */
public class ColInfoVo {
	
	private int colIndex;
	private int rowIndex;
	private String fieldName;
	private String fieldLabel;
	private String value;
	
	private String dateType;
	private CellFormat format;
	private boolean lock;
	private List<String> dropdown;
	
	private String message;
	private boolean success;
	
	public ColInfoVo(){
		
	}
	public ColInfoVo(String fieldName, String fieldLabel){
		this(fieldName, fieldLabel, false);
	}
	
	public ColInfoVo(String fieldName, String fieldLabel, boolean lock){
		this.fieldName = fieldName;
		this.fieldLabel = fieldLabel;
		this.lock = lock;
	}
	
	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}

	public int getColIndex() {
		return colIndex;
	}
	public ColInfoVo setColIndex(int colIndex) {
		this.colIndex = colIndex;
		return this;
	}
	
	public int getRowIndex() {
		return rowIndex;
	}
	public ColInfoVo setRowIndex(int rowIndex) {
		this.rowIndex = rowIndex;
		return this;
	}
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public String getValue() {
		return value;
	}
	public ColInfoVo setValue(String value) {
		this.value = value;
		return this;
	}

	public String getMessage() {
		return message;
	}
	public ColInfoVo setMessage(String message) {
		this.message = message;
		return this;
	}
	public boolean isSuccess() {
		return success;
	}
	public ColInfoVo setSuccess(boolean success) {
		this.success = success;
		return this;
	}

	public String getFieldLabel() {
		return fieldLabel;
	}

	public ColInfoVo setFieldLabel(String fieldLabel) {
		this.fieldLabel = fieldLabel;
		return this;
	}

	public String getDateType() {
		return dateType;
	}

	public ColInfoVo setDateType(String dateType) {
		this.dateType = dateType;
		return this;
	}

	public CellFormat getFormat() {
		return format;
	}

	public ColInfoVo setFormat(CellFormat<?> format) {
		this.format = format;
		return this;
	}

	public boolean isLock() {
		return lock;
	}

	public ColInfoVo setLock(boolean lock) {
		this.lock = lock;
		return this;
	}

	public List<String> getDropdown() {
		return dropdown;
	}

	public ColInfoVo setDropdown(List<String> dropdown) {
		this.dropdown = dropdown;
		return this;
	}
}
