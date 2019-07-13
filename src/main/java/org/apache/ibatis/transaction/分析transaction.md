


事务管理,

sqlSession对外提供事务
 --> 委托给 executor
    --> 委托给 transaction处理  
            -- getConnection
            -- commit
            -- rollback



