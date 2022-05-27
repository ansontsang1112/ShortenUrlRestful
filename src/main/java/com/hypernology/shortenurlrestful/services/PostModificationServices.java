package com.hypernology.shortenurlrestful.services;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class PostModificationServices {
    public Map userRequestModify(Map requestBody) {
        // Check and convert timestamp to PHP based Unix time
        try {
            Long timestamp = Long.parseLong(requestBody.get("timestamp").toString());
            String modifiedTimestamp = String.valueOf((timestamp / 1000L));
            requestBody.replace("timestamp", modifiedTimestamp);
            return requestBody;
        } catch (NumberFormatException e) {
            HashMap hashMap = new HashMap();
            hashMap.put("code", 500);
            hashMap.put("message", e.getMessage());
            return hashMap;
        }
    }
}
