package org.ltsh.core.core.db.jdbc.bean;

/**
 * 数据表字段信息
 * @author YeChao
 */
public class DBTableColumn {
	private String columnName;	// 字段名
	private String dataType;	// 字段类型
	private int length;		//字段长度
	private int precision;		//数字长度
	private int scale;			//数字精度
	private boolean nullable;		//是否允许为空
	private String comment;	//列注释
	
	
	@Override
	public String toString() {
		return "DBTableColumn [columnName=" + columnName + ", dataType=" + dataType + ", length=" + length
				+ ", precision=" + precision + ", scale=" + scale + ", nullable=" + nullable + ", comment=" + comment
				+ "]";
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getColumnName() {
		return columnName;
	}
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public boolean isNullable() {
		return nullable;
	}

	public void setNullable(boolean nullable) {
		this.nullable = nullable;
	}

	public int getPrecision() {
		return precision;
	}

	public void setPrecision(int precision) {
		this.precision = precision;
	}

	public int getScale() {
		return scale;
	}

	public void setScale(int scale) {
		this.scale = scale;
	}
}
