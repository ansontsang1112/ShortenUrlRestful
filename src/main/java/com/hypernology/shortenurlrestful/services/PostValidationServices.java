package com.hypernology.shortenurlrestful.services;

import com.hypernology.shortenurlrestful.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class PostValidationServices {
    @Autowired
    private UserRepository userRepository;

    public boolean userValidateRequest(Map requestBody) {
        if(requestBody.isEmpty()) return false;
        if(!requestBody.containsKey("uid")) return false;
        if(!requestBody.containsKey("username")) return false;
        if(!requestBody.containsKey("discord_id")) return false;
        if(!requestBody.containsKey("member_id")) return false;
        if(!requestBody.containsKey("email")) return false;
        if(!requestBody.containsKey("timestamp")) return false;

        return true;
    }

    public boolean duplicatedUser(Map requestBody) {
        if(!userRepository.queryUserByID(requestBody.get("uid")).isEmpty()) return true;
        if(!userRepository.queryUserByDiscordId(requestBody.get("discord_id")).isEmpty()) return true;
        return false;
    }
}
