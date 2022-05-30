package com.hypernology.shortenurlrestful.services;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.net.URI;
import java.util.Map;

@Service
public class ResponseEntityService implements Serializable {
    public <P, K, M> ResponseEntity<?> responseEntityGenerator(P path, K key, Map<K, M> requestMap) {
        return ResponseEntity.created(URI.create(String.format(path.toString(), requestMap.get(key.toString())))).body(requestMap);
    }
}
