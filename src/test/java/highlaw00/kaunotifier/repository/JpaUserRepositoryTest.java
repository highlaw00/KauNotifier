package highlaw00.kaunotifier.repository;

import highlaw00.kaunotifier.entity.User;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@Transactional
public class JpaUserRepositoryTest {
    private final UserRepository repository;

    @Autowired
    public JpaUserRepositoryTest(UserRepository repository) {
        this.repository = repository;
    }

    @Test
    void save() {
        User user = new User();
        user.setEmail("choiyool00@gmail.com");

        User savedUser = repository.save(user);

        Assertions.assertThat(savedUser.getEmail()).isEqualTo(user.getEmail());
    }

    @Test
    void findById() {
        User user = new User();
        user.setEmail("choiyool00@gmail.com");

        User savedUser = repository.save(user);

        User findUser = repository.findById(savedUser.getUserId()).get();

        Assertions.assertThat(savedUser.getUserId()).isEqualTo(findUser.getUserId());
    }

    @Test
    void findAll() {
        User user1 = new User();
        user1.setEmail("choiyool00@gmail.com");

        User user2 = new User();
        user2.setEmail("choiyool00@naver.com");

        List<User> beforeUsers = repository.findAll();

        repository.save(user1);
        repository.save(user2);

        List<User> afterUsers = repository.findAll();
        Assertions.assertThat(beforeUsers.size() + 2).isEqualTo(afterUsers.size());
    }

    @Test
    void deleteById() {
        User user = new User();
        user.setEmail("choiyool00@gmail.com");

        User savedUser = repository.save(user);

        User deletedUser = repository.deleteById(savedUser.getUserId()).get();

        boolean isEmpty = repository.findById(deletedUser.getUserId()).isEmpty();

        Assertions.assertThat(isEmpty).isTrue();
    }
}
