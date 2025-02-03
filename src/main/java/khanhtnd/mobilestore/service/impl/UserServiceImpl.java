package khanhtnd.mobilestore.service.impl;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import khanhtnd.mobilestore.dto.request.UserRegister;
import khanhtnd.mobilestore.dto.response.AuthenticationResponse;
import khanhtnd.mobilestore.exception.CustomException;
import khanhtnd.mobilestore.exception.common.DuplicateException;
import khanhtnd.mobilestore.exception.common.NotFoundException;
import khanhtnd.mobilestore.model.User;
import khanhtnd.mobilestore.repository.UserRepository;
import khanhtnd.mobilestore.service.UserServiceAdvice;
import khanhtnd.mobilestore.utils.Message;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;

@Slf4j
@Service
public class UserServiceImpl implements UserServiceAdvice {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @NonFinal
    @Value("${secret.key}")
    protected String signerKey;

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


    @Override
    public AuthenticationResponse login(String username, String password) {
        Optional<User> userOptional = userRepository.findByUsername(username);

        if (userOptional.isEmpty()) {
            throw new NotFoundException(Message.MSG_405, 0);
        }

        if (!passwordEncoder.matches(password, userOptional.get().getPasswordHash())) {
            throw new NotFoundException(Message.MSG_406, 0);
        }
        var token = generateToken(userOptional.get());
        return AuthenticationResponse.builder()
                .token(token)
                .authenticated(true)
                .build();
    }

    private String generateToken(User user) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issuer("mobile store")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()
                ))
                .claim("scope", user.getRole().name())
                .build();
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(header, payload);
        try {
            jwsObject.sign(new MACSigner(signerKey.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error(e.getMessage(), e);
            throw new CustomException(Message.MSG_500);
        }
    }
}
