package khanhtnd.mobilestore.controller;

import khanhtnd.mobilestore.dto.request.UserRegister;
import khanhtnd.mobilestore.dto.response.Response;
import khanhtnd.mobilestore.model.User;
import khanhtnd.mobilestore.service.UserServiceAdvice;
import khanhtnd.mobilestore.utils.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserServiceAdvice userServiceAdvice;

    @Autowired
    public UserController(UserServiceAdvice userServiceAdvice) {
        this.userServiceAdvice = userServiceAdvice;
    }

    @PostMapping("/register")
    public ResponseEntity<Response<Integer>> register(@ModelAttribute UserRegister userRegister) {
        int userRegisterId = userServiceAdvice.register(userRegister);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(userRegisterId)
                .toUri();

        Response<Integer> response = Response.<Integer>builder()
                .code(Message.MSG_201.getCode())
                .description(Message.MSG_201.getDescription())
                .data(userRegisterId)
                .build();

        return ResponseEntity.created(location).body(response);

    }
}
