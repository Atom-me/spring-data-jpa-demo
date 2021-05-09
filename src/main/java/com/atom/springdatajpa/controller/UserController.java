package com.atom.springdatajpa.controller;

import com.atom.springdatajpa.entity.User;
import com.atom.springdatajpa.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * @author Atom
 */
@RestController
public class UserController {

    @Resource
    private UserRepository userRepository;


    @GetMapping("/user/{id}")
    public User getUser(@PathVariable(name = "id") Integer id) {
        Optional<User> byId = userRepository.findById(id);
        return byId.orElse(null);
    }


    @PostMapping("/create")
    public ResponseEntity<User> create(@RequestBody User user) {
        user.setId(33);//设置ID也不会生效，应为 User id字段设置了自动增长
        User save = userRepository.save(user);
        return ResponseEntity.ok(save);
    }
}
