package com.example.userserver.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public UserInfo signUpUser(@RequestBody UserRequest userRequest) {
        return userService.createUser(userRequest);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserInfo> getUserInfo(@PathVariable("id") int id) {
        log.info("UserController.getUserInfo : " + id);
        UserInfo user = userService.getUser(id);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(user);
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<UserInfo> getUserInfoByName(@PathVariable("name") String name) {
        UserInfo user = userService.getUserByName(name);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(user);
    }

    @PostMapping("/signIn")
    public UserInfo signIn(@RequestBody UserRequest signInRequest) {
        return userService.signIn(signInRequest);
    }
}