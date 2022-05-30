package com.hypernology.shortenurlrestful.controller;

import com.hypernology.shortenurlrestful.component.StaticUtils;
import com.hypernology.shortenurlrestful.model.User;
import com.hypernology.shortenurlrestful.repository.UserRepository;
import com.hypernology.shortenurlrestful.services.PostValidationServices;
import com.hypernology.shortenurlrestful.services.ResponseEntityService;
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
import java.util.Optional;

@RestController
public class UserController {
    private static final String PRODUCES = "application/json";

    @Resource
    private UserRepository userRepository;

    @Autowired
    private SerializationServices serializationServices;

    @Autowired
    private PostValidationServices postValidationServices;

    @Autowired
    private ResponseEntityService responseEntityService;

    @RequestMapping(value = {"/user/findAll", "/user/findAll/{showDisabled}"}, method = RequestMethod.GET, produces = PRODUCES)
    public List<User> users(@PathVariable Optional<Boolean> showDisabled) {
        Boolean showDisable = showDisabled.orElseGet(()->false);
        List<User> userList = userRepository.queryAllUser(showDisable);
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
            return responseEntityService.responseEntityGenerator("/user/add/%s", "code", nullObject);
        }

        // Check if user exist
        if(!postValidationServices.duplicatedUser(serializedUser)) {
            User persistedUser = userRepository.save(serializedUser);
            return ResponseEntity.created(URI.create(String.format("/user/add/%s", persistedUser.getUid()))).body(persistedUser);
        } else {
            Map<String, Object> duplicated = new HashMap<>();
            duplicated.put("code", 409);
            duplicated.put("message", "user object duplicated");
            return responseEntityService.responseEntityGenerator("/user/add/%s", "code", duplicated);
        }
    }

    @RequestMapping(value = "/user/modify", method = RequestMethod.PATCH, produces = PRODUCES)
    public ResponseEntity<?> modifyUser(@RequestBody Map requestBody) {
        Map<String, Object> response = new HashMap<>();
        // Not nullable check
        if(!requestBody.containsKey("id") || !requestBody.containsKey("updateKey") || !requestBody.containsKey("updateValue")) {
            response.put("code", 501);
            response.put("message", "{id}, {updateKey} or {updateValue} cannot be null or empty");
            return responseEntityService.responseEntityGenerator("/user/modify/%s", "code", response);
        }

        if(!postValidationServices.isUserExist(requestBody.get("id").toString())) {
            response.put("code", 404);
            response.put("message", "user not found");
            return responseEntityService.responseEntityGenerator("/user/modify/%s", "code", response);
        }

        String uidResponse = userRepository.update(requestBody.get("updateKey"), requestBody.get("updateValue"), requestBody.get("id"));

        response.put("code", 201);
        response.put("message", uidResponse + " is updated successfully");
        return responseEntityService.responseEntityGenerator("/user/modify/%s", "code", response);
    }

    @RequestMapping(value = "/user/disable/{userId}", method = RequestMethod.PATCH, produces = PRODUCES)
    public <T> ResponseEntity<?> disableUser(@PathVariable Optional<T> userId) {
        Map<String, Object> response = new HashMap<>();
        // Not nullable check
        if(!userId.isPresent()) {
            response.put("code", 501);
            response.put("message", "{userId} cannot be null");
            return responseEntityService.responseEntityGenerator("/user/disable/%s", "code", response);
        }

        if(!postValidationServices.isUserExist(userId.get().toString())) {
            response.put("code", 404);
            response.put("message", "user not found");
            return responseEntityService.responseEntityGenerator("/user/disable/%s", "code", response);
        }

        String uidDisabled = userRepository.disable(userId.get().toString());

        response.put("code", 201);
        response.put("message", uidDisabled + " successfully disabled");

        return responseEntityService.responseEntityGenerator("/user/disable/%s", "code", response);
    }

    @RequestMapping(value = "/user/delete/{userId}", method = RequestMethod.DELETE, produces = PRODUCES)
    public <T> ResponseEntity<?> deleteUser(@PathVariable Optional<T> userId) {
        Map<String, Object> response = new HashMap<>();
        // Not nullable check
        if(!userId.isPresent()) {
            response.put("code", 501);
            response.put("message", "{userId} cannot be null");
            return responseEntityService.responseEntityGenerator("/user/delete/%s", "code", response);
        }

        if(!postValidationServices.isUserExist(userId.get().toString())) {
            response.put("code", 404);
            response.put("message", "user not found");
            return responseEntityService.responseEntityGenerator("/user/delete/%s", "code", response);
        }

        String uidDisabled = userRepository.delete(userId.get().toString());

        response.put("code", 201);
        response.put("message", uidDisabled + " successfully deleted");

        return responseEntityService.responseEntityGenerator("/user/delete/%s", "code", response);
    }
}
