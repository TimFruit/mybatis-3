


Builder用于解析各种配置， 构建相应的配置实体

XMLConfigBuilder 用于解析mybatis-config.xml配置文件， 构建Configuration对象
XMLMapperBuilder 用于解析mapper.xml配置文件， 将解析结果添加进Configuration
XMLStatementBuilder 用于解析mapper.xml文件中的sql语句， 将对应mappedStatement添加进Configuration
XMLIncludeTransformer 用于解决<include/> sql引用

MapperAnnotationBuilder 用于解析mapper接口文件， 
它会先查找该类所在包下的mapper.xml配置文件， 进而解析
然后再解析mapper接口文件









