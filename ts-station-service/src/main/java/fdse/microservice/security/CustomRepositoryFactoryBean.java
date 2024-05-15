
package fdse.microservice.security;

import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;
import javax.persistence.EntityManager;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.jpa.repository.JpaRepository;

public class CustomRepositoryFactoryBean<T extends JpaRepository<S, ID>, S, ID> extends JpaRepositoryFactoryBean<T, S, ID> {
    public CustomRepositoryFactoryBean(Class<? extends T> repositoryInterface) {
        super(repositoryInterface);
    }

    @Override
    protected RepositoryFactorySupport createRepositoryFactory(EntityManager entityManager) {
        JpaRepositoryFactory jpaFactory = new JpaRepositoryFactory(entityManager);
        jpaFactory.addRepositoryProxyPostProcessor(new SecrecyPostProcessor());
        return jpaFactory;
    }
}
