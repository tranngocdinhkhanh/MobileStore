package khanhtnd.mobilestore.service.impl;

import khanhtnd.mobilestore.dto.request.UserRegister;
import khanhtnd.mobilestore.exception.common.DuplicateException;
import khanhtnd.mobilestore.model.User;
import khanhtnd.mobilestore.repository.UserRepository;
import khanhtnd.mobilestore.service.UserServiceAdvice;
import khanhtnd.mobilestore.utils.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserServiceAdvice {
    private final UserRepository userRepository;
private final PasswordEncoder passwordEncoder;
    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public int register(UserRegister userRegister) {
        if (userRepository.existsByUsername(userRegister.getUsername())) {
            throw new DuplicateException(Message.MSG_410, userRegister.getUsername());
        }
        String password = passwordEncoder.encode(userRegister.getPassword());
        userRegister.setPassword(password);
        User user = userRegister.mapToUser();
        userRepository.save(user);
        return user.getId();
    }
}
