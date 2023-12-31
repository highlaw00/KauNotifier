package highlaw00.kaunotifier.repository;

import highlaw00.kaunotifier.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    User save(User user);
    Optional<User> findById(Long id);
    List<User> findAll();
    Optional<User> deleteById(Long id);
    Optional<User> findByEmail(String email);
}
