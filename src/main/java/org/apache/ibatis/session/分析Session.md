



SqlSessionFactoryBuilder为源码入口， 用于解析mybatis-config.xml， 然后构建Configuration


binding包, 是用于动态生成代理类, 用于将mapper接口方法和对应的mappedStatement绑定起来

MepperMethod
```
 public Object execute(SqlSession sqlSession, Object[] args) {
    // 最后都是调用sqlSession.insert, update, delete, select


    Object result;
    switch (command.getType()) {
      // 1. 插入
      case INSERT: {
        Object param = method.convertArgsToSqlCommandParam(args);
        result = rowCountResult(sqlSession.insert(command.getName(), param));
        break;
      }

      // 2. 更新
      case UPDATE: {
        Object param = method.convertArgsToSqlCommandParam(args);
        result = rowCountResult(sqlSession.update(command.getName(), param));
        break;
      }

      // 3. 删除
      case DELETE: {
        Object param = method.convertArgsToSqlCommandParam(args);
        result = rowCountResult(sqlSession.delete(command.getName(), param));
        break;
      }

      // 4. 查询
      case SELECT:
        if (method.returnsVoid() && method.hasResultHandler()) {
          executeWithResultHandler(sqlSession, args);
          result = null;
        } else if (method.returnsMany()) {
          result = executeForMany(sqlSession, args);
        } else if (method.returnsMap()) {
          result = executeForMap(sqlSession, args);
        } else if (method.returnsCursor()) {
          result = executeForCursor(sqlSession, args);
        } else {
          Object param = method.convertArgsToSqlCommandParam(args);
          result = sqlSession.selectOne(command.getName(), param);
          if (method.returnsOptional()
              && (result == null || !method.getReturnType().equals(result.getClass()))) {
            result = Optional.ofNullable(result);
          }
        }
        break;

      // flush
      case FLUSH:
        result = sqlSession.flushStatements();
        break;


      default:
        throw new BindingException("Unknown execution method for: " + command.getName());
    }
    if (result == null && method.getReturnType().isPrimitive() && !method.returnsVoid()) {
      throw new BindingException("Mapper method '" + command.getName()
          + " attempted to return null from a method with a primitive return type (" + method.getReturnType() + ").");
    }
    return result;
  }

```




// org.apache.ibatis.session.defaults.DefaultSqlSession
DefaultSqlSession#selectList()等方法, 是真正执行sql语句的入口

```
  // -----------------------------
  // 最后都是从Configuration中获取对应的mappedStatement
  // 然后由执行器调用
  // -----------------------------
  @Override
  public <E> List<E> selectList(String statement, Object parameter, RowBounds rowBounds) {
    try {
      MappedStatement ms = configuration.getMappedStatement(statement);
      return executor.query(ms, wrapCollection(parameter), rowBounds, Executor.NO_RESULT_HANDLER);
    } catch (Exception e) {
      throw ExceptionFactory.wrapException("Error querying database.  Cause: " + e, e);
    } finally {
      ErrorContext.instance().reset();
    }
  }
```