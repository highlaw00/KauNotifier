package highlaw00.kaunotifier;

import highlaw00.kaunotifier.repository.*;
import highlaw00.kaunotifier.service.*;
import jakarta.persistence.EntityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class SpringConfig {
    private final DataSource dataSource;
    private final EntityManager em;

    public SpringConfig(DataSource dataSource, EntityManager em) {
        this.dataSource = dataSource;
        this.em = em;
    }
    @Bean
    public SubscriptionService subscriptionService() {
        return new SubscriptionServiceImpl(subscriptionRepository());
    }

    @Bean
    public SourceService sourceService() {
        return new SourceServiceImpl(sourceRepository());
    }

    @Bean
    public UserService userService() { return new UserServiceImpl(userRepository()); }

    @Bean
    public SubscriptionRepository subscriptionRepository() {
        return new JpaSubscriptionRepository(em);
    }

    @Bean
    public UserRepository userRepository() { return new JpaUserRepository(em); }

    @Bean
    public SourceRepository sourceRepository() { return new JpaSourceRepository(em); }
}
