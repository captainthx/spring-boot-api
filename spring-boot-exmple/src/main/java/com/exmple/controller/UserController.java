package com.exmple.controller;

import com.exmple.entity.User;
import com.exmple.exception.BaseException;
import com.exmple.exception.UserException;
import com.exmple.model.*;
import com.exmple.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping
    public ResponseEntity<List<User>> getAll()  {
        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(userService.findById(id));
    }
    @GetMapping("/refresh-token")
    public ResponseEntity<String> refreshToken() throws UserException {
        String refreshToken = userService.refreshToken();
        return ResponseEntity.ok(refreshToken);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody RequestLogin request) throws BaseException {
        String response = userService.login(request);
        return ResponseEntity.ok(response);
    }

    // register
    @PostMapping
    public ResponseEntity<RegisterResponse> create(@RequestBody RegisterRequest request) throws BaseException {
        RegisterResponse response = userService.register(request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UpdateResponse> update(@PathVariable Integer id, @RequestBody UpdateRequest request) throws BaseException {
        return ResponseEntity.status(HttpStatus.OK).body(userService.update(id, request));
    }


}
