package com.hypernology.shortenurlrestful.services;

import com.hypernology.shortenurlrestful.component.StaticUtils;
import com.hypernology.shortenurlrestful.model.ShortenUrl;
import com.hypernology.shortenurlrestful.model.User;
import com.hypernology.shortenurlrestful.repository.ShortenUrlRepository;
import com.hypernology.shortenurlrestful.repository.UserRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Map;

@Service
public class SerializationServices implements Serializable {
    @Autowired
    PostValidationServices postValidationServices;

    @Autowired
    PostModificationServices postModificationServices;

    @Autowired
    TitleFetchingServices titleFetchingServices;

    @Resource
    ShortenUrlRepository shortenUrlRepository;

    @Resource
    UserRepository userRepository;


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

    public ShortenUrl urlDeserializationServices(Map shortenUrl) {
        if(postValidationServices.shortenUrlValidateRequest(shortenUrl)) {
            Map modifiedUrlMap = postModificationServices.shortenUrlModify(shortenUrl);

            ShortenUrl deserializedShortenUrl = new ShortenUrl();

            deserializedShortenUrl.setKey(uniqueRedisKey());
            deserializedShortenUrl.setUrl((String) modifiedUrlMap.get("url"));
            deserializedShortenUrl.setClicks(0);
            deserializedShortenUrl.setTitle(titleFetchingServices.fetchUrlTitle(deserializedShortenUrl.getUrl()));
            deserializedShortenUrl.setTimestamp((String) modifiedUrlMap.get("timestamp"));
            deserializedShortenUrl.setStatus("active");
            deserializedShortenUrl.setIp("N/A");

            if(userRepository.queryUserByDiscordId(modifiedUrlMap.get("discordId")).isEmpty()) {
                deserializedShortenUrl.setUserid("anonymous");
            } else {
                String userid = userRepository.queryUserByDiscordId((String) modifiedUrlMap.get("discordId")).get(0).getUid();
                deserializedShortenUrl.setUserid(userid);
            }

            return deserializedShortenUrl;
        } else {
            return new ShortenUrl();
        }
    }

    private String uniqueRedisKey() {
        String redisKey = RandomStringUtils.random(6, StaticUtils.alphabetic);
        if(shortenUrlRepository.findByKey(redisKey).getKey() != null) {
            uniqueRedisKey();
        }
        return redisKey;
    }
}
