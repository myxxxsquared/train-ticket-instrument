
package contacts.security;

import org.aopalliance.intercept.MethodInvocation;

public abstract class SubSecrecyFilter {
    public static Object doFilter(MethodInvocation invocation) throws Throwable {
        Object obj = invocation.proceed();  // 执行原方法
        return obj;
    }
}
