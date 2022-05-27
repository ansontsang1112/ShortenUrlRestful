package com.hypernology.shortenurlrestful.controller;

import com.hypernology.shortenurlrestful.model.User;
import com.hypernology.shortenurlrestful.repository.UserRepository;
import net.minidev.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.net.URI;
import java.util.List;

@RestController
public class UserController {
    private static final String PRODUCES = "application/json";

    @Resource
    private UserRepository userRepository;

    @RequestMapping(value = "/user/findAll", method = RequestMethod.GET, produces = PRODUCES)
    public List<User> users() {
        List<User> userList = userRepository.queryAllUser();
        return userList;
    }

    @RequestMapping(value = "/user/{method}/{key}", method = RequestMethod.GET, produces = PRODUCES)
    public <T> User getUserByKey(@PathVariable String method, @PathVariable T key) {
        switch (method) {
            case "discordId":
                return (userRepository.queryUserByDiscordId(key).isEmpty()) ? new User() : userRepository.queryUserByDiscordId(key).get(0);
            case "memberId":
                return (userRepository.queryUserByMemberId(key).isEmpty()) ? new User() : userRepository.queryUserByMemberId(key).get(0);
            case "id":
                return (userRepository.queryUserByID(key).isEmpty()) ? new User() : userRepository.queryUserByID(key).get(0);
            default:
                return new User();
        }
    }

    @RequestMapping(value = "/user/add", method = RequestMethod.POST, produces = PRODUCES)
    public ResponseEntity<User> addUser(@RequestBody User user) {
        User persistedUser = userRepository.save(user);
        return ResponseEntity.created(URI.create(String.format("/user/add/%s", user.getUid()))).body(persistedUser);
    }
}
