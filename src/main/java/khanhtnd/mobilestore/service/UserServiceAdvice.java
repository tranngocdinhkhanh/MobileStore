package khanhtnd.mobilestore.service;

import khanhtnd.mobilestore.dto.request.UserRegister;
import khanhtnd.mobilestore.dto.response.AuthenticationResponse;
import khanhtnd.mobilestore.model.User;

import java.util.Optional;

public interface UserServiceAdvice {
    int register(UserRegister user);
    AuthenticationResponse login(String username, String password);
}
