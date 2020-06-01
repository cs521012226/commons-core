package ${package};

import com.alibaba.fastjson.JSON;
import org.ltsh.core.business.vo.Record;

public class ${entityName} extends Record{

<#list entityField as field>
	/**
	*	${field.fieldComment}
	*/
	public void set${field.capFieldName}(${field.fieldType} ${field.fieldName}){
		set("${field.dbFieldName}", ${field.fieldName});
	}
	/**
	*	${field.fieldComment}
	*/
	public ${field.fieldType} get${field.capFieldName}(){
		<#if field.fieldType == "java.lang.Long">
		return getLong("${field.dbFieldName}");
		<#else>
		return get("${field.dbFieldName}");
		</#if>
	}
</#list>

	@Override
	public String toString() {
		return JSON.toJSONString(getColumns());
	}
}
