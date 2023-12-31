package highlaw00.kaunotifier.service;

import highlaw00.kaunotifier.entity.User;

import java.util.Optional;

public interface UserService {
    User join(User user);
    Optional<User> findOne(Long userId);
    Optional<User> findOneByEmail(String email);
}
