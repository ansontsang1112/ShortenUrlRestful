package com.hypernology.shortenurlrestful.repository;

import com.hypernology.shortenurlrestful.model.ShortenUrl;
import com.hypernology.shortenurlrestful.services.RedisServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ShortenUrlRepositoryImpl implements ShortenUrlRepository {
    @Autowired
    RedisTemplate<String, ?> redisTemplate;

    @Autowired
    RedisServices redisServices;

    @Override
    public List<ShortenUrl> findAll() {
        List<String> keys = redisServices.keyHandler(redisTemplate.keys("*"));
        List<ShortenUrl> urlList = new ArrayList<>();
        for(String key : keys) {
            urlList.add(findByKey(key));
        }

        return urlList;
    }

    @Override
    public <T> ShortenUrl findByKey(T key) {
        Map redisMap = redisTemplate.opsForHash().entries(key.toString());

        if(redisMap.isEmpty()) return new ShortenUrl();

        return redisServices.objectConverter(redisMap);
    }

    @Override
    public String save(ShortenUrl shortenUrl) {
        String redisKey = shortenUrl.getKey();

        redisTemplate.opsForHash().put(redisKey, "click", String.valueOf(shortenUrl.getClicks()));
        redisTemplate.opsForHash().put(redisKey, "ip", shortenUrl.getIp());
        redisTemplate.opsForHash().put(redisKey, "status", shortenUrl.getStatus());
        redisTemplate.opsForHash().put(redisKey, "timestamp", shortenUrl.getTimestamp());
        redisTemplate.opsForHash().put(redisKey, "title", shortenUrl.getTitle());
        redisTemplate.opsForHash().put(redisKey, "url", shortenUrl.getUrl());
        redisTemplate.opsForHash().put(redisKey, "userid", shortenUrl.getUserid());

        return redisKey;
    }

    @Override
    public <K, V> ShortenUrl update(K key, List<V> valueHash) {
        return null;
    }
}
