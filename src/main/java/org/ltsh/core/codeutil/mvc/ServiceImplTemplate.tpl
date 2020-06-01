package ${package};

import ${daoPackage}.${entityName}Dao;
import ${superPackage}.${entityName}Service;

public class ${entityName}ServiceImpl implements ${entityName}Service{

	private ${entityName}Dao ${entityNameLower}Dao;
	
	public ${entityName}Dao get${entityName}Dao(){
		return ${entityNameLower}Dao;
	}
	public void set${entityName}Dao(${entityName}Dao ${entityNameLower}Dao){
		this.${entityNameLower}Dao = ${entityNameLower}Dao;
	}
}
