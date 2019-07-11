/**
 *    Copyright 2009-2015 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.apache.ibatis.plugin;

import java.util.Properties;

/**
 * @author Clinton Begin
 */
public interface Interceptor {

  // 拦截方法
  Object intercept(Invocation invocation) throws Throwable;

  // FIXME 为target动态生成对应的代理类并返回, 其中代理类的实现方法为本拦截器
  // InterceptorChain#pluginAll()
  // Plugin#wrap(Object target, Interceptor interceptor)
  Object plugin(Object target);

  void setProperties(Properties properties);

}
