package com.hypernology.shortenurlrestful.services;

import com.hypernology.shortenurlrestful.component.StaticUtils;
import com.hypernology.shortenurlrestful.model.User;
import org.apache.commons.lang3.RandomStringUtils;
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
            deserializedUser.setUid(RandomStringUtils.random(13, StaticUtils.alphabetic));
            deserializedUser.setEmail((String) modifiedUserMap.get("email"));
            deserializedUser.setStatus("Active");
            deserializedUser.setMemberId("N/A");
            deserializedUser.setDiscordId((String) modifiedUserMap.get("discord_id"));
            deserializedUser.setUsername((String) modifiedUserMap.get("username"));
            deserializedUser.setTimestamp((String) modifiedUserMap.get("timestamp"));

            return deserializedUser;
        }  else {
            return new User();
        }
    }
}
