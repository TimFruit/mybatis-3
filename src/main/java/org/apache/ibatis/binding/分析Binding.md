

该包主要用于动态生成mapper接口对应的代理类, 用于执行sql语句

MapperRegistry是mapper注册表, 是Configuration中的一个属性,用于维护所有的mapper
mapper接口, 以及对应的mapper代理类工厂

注意事项, 每次获取对应的mapper类, 都会动态生成对应的代理类 #FIXME 
```
  public <T> T getMapper(Class<T> type, SqlSession sqlSession) {
    //在注册表中获取对应的代理工厂
    final MapperProxyFactory<T> mapperProxyFactory = (MapperProxyFactory<T>) knownMappers.get(type);
    if (mapperProxyFactory == null) {
      throw new BindingException("Type " + type + " is not known to the MapperRegistry.");
    }

    // 生成对应的代理类
    try {
      return mapperProxyFactory.newInstance(sqlSession);
    } catch (Exception e) {
      throw new BindingException("Error getting mapper instance. Cause: " + e, e);
    }
  }
```




MapperProxy为代理类的处理方法, 里面有缓存对应的方法
```
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    try {
      if (Object.class.equals(method.getDeclaringClass())) {// 如果不是代理类, 则直接调用
        return method.invoke(this, args);
      } else if (isDefaultMethod(method)) {
        return invokeDefaultMethod(proxy, method, args);
      }
    } catch (Throwable t) {
      throw ExceptionUtil.unwrapThrowable(t);
    }
    // 从缓存中获取方法
    // new MapperMethod(mapperInterface, method, sqlSession.getConfiguration())
    // 该方法仅仅只是调用方法名绑定的statement #TODO  sqlSession.select()
    final MapperMethod mapperMethod = cachedMapperMethod(method);
    return mapperMethod.execute(sqlSession, args);
  }

  private MapperMethod cachedMapperMethod(Method method) {
    // 如果不存在, 则new MapperMethod(mapperInterface, method, sqlSession.getConfiguration())
    return methodCache.computeIfAbsent(method, k -> new MapperMethod(mapperInterface, method, sqlSession.getConfiguration()));
  }
```



MapperMethod用于执行对应的方法调用, 真正的执行sql语句的方法入口
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







