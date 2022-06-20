package com.hypernology.shortenurlrestful.services;

import com.hypernology.shortenurlrestful.component.StaticUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class PostModificationServices {
    @Autowired
    private TitleFetchingServices titleFetchingServices;

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

    public Map shortenUrlModify(Map requestBody) {
        try {
            // Check and convert timestamp to PHP based Unix time
            Long timestamp = Long.parseLong(requestBody.get("timestamp").toString());
            String modifiedTimestamp = String.valueOf((timestamp / 1000L));

            // Check if URL valid and fetch title
            if(!StaticUtils.urlValidation(requestBody.get("url").toString())) {
                throw new NumberFormatException("URL are not valid");
            }

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
