package ${package};

import com.jfinal.plugin.activerecord.Model;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public class ${entityName} extends Model<${entityName}> {

<#list entityField as field>
	public void set${field.capFieldName}(${field.fieldType} ${field.fieldName}){
		set("${field.dbFieldName}", ${field.fieldName});
	}
	public ${field.fieldType} get${field.capFieldName}(){
		<#if field.fieldType == "java.lang.Long">
		return getLong("${field.dbFieldName}");
		<#else>
		return get("${field.dbFieldName}");
		</#if>
	}
</#list>
}
