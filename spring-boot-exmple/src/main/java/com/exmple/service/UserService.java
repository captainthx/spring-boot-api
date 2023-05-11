package com.exmple.service;

import com.exmple.entity.User;
import com.exmple.exception.BaseException;
import com.exmple.exception.UserException;
import com.exmple.mapper.UserMapper;
import com.exmple.model.*;
import com.exmple.repository.UserRepository;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final TokenService tokenService;


    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, TokenService tokenService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
    }

    public User findById(Integer id) {
        return userRepository.findById(id).orElse(null);
    }

    @Cacheable(cacheNames = "user",key="#id")
    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User create(String name, String email, String username, String password, String role) {
        //set data in entity
        User entity = new User();
        entity.setName(name);
        entity.setEmail(email);
        entity.setUsername(username);
        // encode password
        entity.setPassword(passwordEncoder.encode(password));
        entity.setRole(role);
        return userRepository.save(entity);
    }

    public RegisterResponse register(RegisterRequest request) throws BaseException {
        //validate email
        if (Objects.isNull(request.getEmail())) {
            throw UserException.emailNull();
        }
        // check  email duplicate
        if (userRepository.existsByEmail(request.getEmail())) {
            throw UserException.createEmailDuplicated();
        }

        // save to db
        User user = create(request.getName(), request.getEmail(), request.getUsername(), request.getPassword(), request.getRole());

        //mapper
        return UserMapper.INSTANCE.toRegisterResponse(user);
    }

    public String login(RequestLogin request) throws BaseException {
        //validate username
        Optional<User> opt = userRepository.findByUsername(request.getUsername());
        if (opt.isEmpty()) {
            throw UserException.notFound();
        }
        User user = opt.get();
        if (!matchPassword(request.getPassword(), user.getPassword())) {
            throw UserException.loginFailUsernameNotFound();
        }
        // gen jwt token
        return tokenService.token(user);

    }

    public String refreshToken() throws UserException {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        String userId = (String) authentication.getPrincipal();

        Optional<User> opt = userRepository.findById(Integer.getInteger(userId));
        if (opt.isEmpty()) {
            throw UserException.notFound();
        }

        User user = opt.get();
        return tokenService.token(user);
    }

    public boolean matchPassword(String rawPassword, String encodePassword) {
        return passwordEncoder.matches(rawPassword, encodePassword);
    }

    @CachePut(cacheNames = "user" ,key = "#id")
    public UpdateResponse update(Integer id, UpdateRequest request) throws BaseException {
        Optional<User> opt = userRepository.findById(id);
        //validate data
        if (opt.isEmpty()) {
            throw UserException.notFound();
        }

        User entity = opt.get();
        // validate name
        if (Objects.nonNull(request.getName())) {
            entity.setName(request.getName());
        }
        //validate email
        if (Objects.nonNull(request.getEmail())) {
            entity.setEmail(request.getEmail());
        }
        //save to db
        User user = userRepository.save(entity);
        //return mapper
        return UserMapper.INSTANCE.toUpdateResponse(user);
    }
}
