

session包中的执行器类型

```
public enum ExecutorType {
  SIMPLE, REUSE, BATCH
}
```
// simple, 每次都会预编译sql语句, reuse, 会复用之前预编译的sql语句, batch, 批量执行sql语句
三种类型分别对应了SimpleExecutor, ReuseExecutor, BatchExecutor



// SimpleExecutor
```
  @Override
  public <E> List<E> doQuery(MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql) throws SQLException {
    Statement stmt = null;
    try {
      Configuration configuration = ms.getConfiguration();
      // 在这里添加了对应的插件链
      StatementHandler handler = configuration.newStatementHandler(wrapper, ms, parameter, rowBounds, resultHandler, boundSql);
      stmt = prepareStatement(handler, ms.getStatementLog());
      return handler.query(stmt, resultHandler);
    } finally {
      closeStatement(stmt);
    }
  }
```


// SimpleStatementHandler
```
   @Override
    public <E> List<E> query(Statement statement, ResultHandler resultHandler) throws SQLException {
      String sql = boundSql.getSql();
  
      // 这里由statement真正执行对应的Sql
      statement.execute(sql);
  
      // 通过参数传递的结果处理器处理返回结果
      return resultSetHandler.handleResultSets(statement);
    }
```


