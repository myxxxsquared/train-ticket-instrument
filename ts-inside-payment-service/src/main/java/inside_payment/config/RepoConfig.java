
package inside_payment.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import inside_payment.security.CustomRepositoryFactoryBean;

@Configuration
@EnableJpaRepositories(
    basePackages = "inside_payment.repository",
    repositoryFactoryBeanClass = CustomRepositoryFactoryBean.class
)
public class RepoConfig {
    // 如果有其他配置，也可以放在这里
}
