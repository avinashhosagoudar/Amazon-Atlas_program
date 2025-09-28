package DAY37.Task001;

import java.util.Date;

public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User02 registerUser(User02 user) {
        user.setRegistrationDate(new Date());
        return userRepository.save(user);
    }
}