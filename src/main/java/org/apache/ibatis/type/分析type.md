

核心接口, 类型处理器: TypeHandler
```
public interface TypeHandler<T> {

   // 设置参数, 预编译类型 
  void setParameter(PreparedStatement ps, int i, T parameter, JdbcType jdbcType) throws SQLException;

  // 获取转换返回结果
  T getResult(ResultSet rs, String columnName) throws SQLException;

  // 获取转换返回结果
  T getResult(ResultSet rs, int columnIndex) throws SQLException;

  // 获取转换返回结果
  T getResult(CallableStatement cs, int columnIndex) throws SQLException;

}

```



BaseTypeHandler, 基本类型处理其, 主要是封装了通用操作, 用于处理空值.

自定义类型处理器的时候,可以继承BaseTypeHandler


然后在mybatis-config.xml中可以配置对应的类型处理器, 或者在Configuration中设置




