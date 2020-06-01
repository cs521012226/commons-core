sqlldr userid=${tnsname}
control=${tableName}.ctrl 
log=${tableName}.log 
bad=${tableName}.bad 
direct=true 
bindsize=20971520 
readsize=20971520 
streamsize=20971520