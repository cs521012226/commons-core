load data
CHARACTERSET 'UTF8'
infile '${dataFilePath}${dataFileName}'
${loadType} into table ${tableSchema}.${tableName} 
fields terminated by '${separate}' 
trailing nullcols 
(

<#list columnField as field>
	${field.fieldName} ${field.fieldType}
</#list>
)