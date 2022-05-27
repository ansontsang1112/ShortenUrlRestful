package com.hypernology.shortenurlrestful.repository;

import com.hypernology.shortenurlrestful.model.ShortenUrl;
import com.hypernology.shortenurlrestful.services.RedisServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
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
        return redisServices.objectConverter(redisMap);
    }

    @Override
    public ShortenUrl save(ShortenUrl shortenUrl) {
        return null;
    }

    @Override
    public <K, V> ShortenUrl update(K key, List<V> valueHash) {
        return null;
    }
}
