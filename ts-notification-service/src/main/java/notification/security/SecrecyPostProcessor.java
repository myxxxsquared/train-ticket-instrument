
package notification.security;

import org.springframework.aop.framework.ProxyFactory;
import org.springframework.data.repository.core.support.RepositoryProxyPostProcessor;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.data.repository.core.RepositoryInformation;

public class SecrecyPostProcessor implements RepositoryProxyPostProcessor {
    @Override
    public void postProcess(ProxyFactory factory, RepositoryInformation repositoryInformation) {
        factory.addAdvice(new MethodInterceptor() {
            @Override
            public Object invoke(MethodInvocation invocation) throws Throwable {
                return SubSecrecyFilter.doFilter(invocation);
            }
        });
    }
}
