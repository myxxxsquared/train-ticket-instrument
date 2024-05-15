
package foodsearch.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import foodsearch.security.CustomRepositoryFactoryBean;

@Configuration
@EnableJpaRepositories(
    basePackages = "foodsearch.repository",
    repositoryFactoryBeanClass = CustomRepositoryFactoryBean.class
)
public class RepoConfig {
    // 如果有其他配置，也可以放在这里
}
