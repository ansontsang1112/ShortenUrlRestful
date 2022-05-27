package com.hypernology.shortenurlrestful.services;

import com.hypernology.shortenurlrestful.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Map;

@Service
public class SerializationServices implements Serializable {
    @Autowired
    PostValidationServices postValidationServices;

    @Autowired
    PostModificationServices postModificationServices;


    public User userDeserializationServices(Map user) {
        if(postValidationServices.userValidateRequest(user)) {
            Map modifiedUserMap = postModificationServices.userRequestModify(user);

            User deserializedUser = new User();
            deserializedUser.setUid((String) modifiedUserMap.get("uid"));
            deserializedUser.setEmail((String) modifiedUserMap.get("email"));
            deserializedUser.setStatus("Active");
            deserializedUser.setMemberId((String) modifiedUserMap.get("member_id"));
            deserializedUser.setDiscordId((String) modifiedUserMap.get("discord_id"));
            deserializedUser.setUsername((String) modifiedUserMap.get("username"));
            deserializedUser.setTimestamp((String) modifiedUserMap.get("timestamp"));

            return deserializedUser;
        }  else {
            return new User();
        }
    }
}
