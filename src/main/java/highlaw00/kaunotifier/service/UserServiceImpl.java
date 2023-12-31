package highlaw00.kaunotifier.service;

import highlaw00.kaunotifier.entity.User;
import highlaw00.kaunotifier.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User join(User user) {
        //TODO: User validation
        return userRepository.save(user);
    }

    @Override
    public Optional<User> findOne(Long userId) {
        return userRepository.findById(userId);
    }

    @Override
    public Optional<User> findOneByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
