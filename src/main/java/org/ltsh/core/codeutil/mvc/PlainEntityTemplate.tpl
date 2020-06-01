package ${package};

import com.alibaba.fastjson.JSON;

public class ${entityName} {

<#list entityField as field>
	/**
	*	${field.fieldComment}
	*/
	private ${field.fieldType} ${field.fieldName};
</#list>

<#list entityField as field>
	/**
	*	${field.fieldComment}
	*/
	public void set${field.capFieldName}(${field.fieldType} ${field.fieldName}){
		this.${field.fieldName} = ${field.fieldName};
	}
	/**
	*	${field.fieldComment}
	*/
	public ${field.fieldType} get${field.capFieldName}(){
		return ${field.fieldName};
	}
</#list>

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}
}
