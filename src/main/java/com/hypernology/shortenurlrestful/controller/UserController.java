package com.hypernology.shortenurlrestful.controller;

import com.hypernology.shortenurlrestful.model.User;
import com.hypernology.shortenurlrestful.repository.UserRepository;
import com.hypernology.shortenurlrestful.services.PostValidationServices;
import com.hypernology.shortenurlrestful.services.SerializationServices;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class UserController {
    private static final String PRODUCES = "application/json";

    @Resource
    private UserRepository userRepository;

    @Autowired
    private SerializationServices serializationServices;

    @Autowired
    private PostValidationServices postValidationServices;

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
    public ResponseEntity<?> addUser(@RequestBody Map requestBody) {
        // Serialized User
        User serializedUser = serializationServices.userDeserializationServices(requestBody);

        if(serializedUser.getUid() == null) {
            Map<String, Object> nullObject = new HashMap<>();
            nullObject.put("code", 500);
            nullObject.put("message", "fail to create user object");
            return ResponseEntity.created(URI.create(String.format("/user/add/%s", nullObject.get("code")))).body(nullObject);
        }

        // Check if user exist
        if(!postValidationServices.duplicatedUser(requestBody)) {
            User persistedUser = userRepository.save(serializedUser);
            return ResponseEntity.created(URI.create(String.format("/user/add/%s", persistedUser.getUid()))).body(persistedUser);
        } else {
            Map<String, Object> duplicated = new HashMap<>();
            duplicated.put("code", 409);
            duplicated.put("message", "user object duplicated");
            return ResponseEntity.created(URI.create(String.format("/user/add/%s", duplicated.get("code")))).body(duplicated);
        }
    }
}
