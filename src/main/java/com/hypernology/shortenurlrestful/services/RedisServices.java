package com.hypernology.shortenurlrestful.services;

import com.hypernology.shortenurlrestful.model.ShortenUrl;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RedisServices {
    public ShortenUrl objectConverter(Map rawData) {
        ShortenUrl url = new ShortenUrl();
        url.setUrl(rawData.get("url").toString());
        url.setClicks(Integer.parseInt((String) rawData.get("clicks")));
        url.setStatus((String) rawData.get("status"));
        url.setTimestamp((String) rawData.get("timestamp"));
        url.setTitle((rawData.get("title").toString() != "") ? rawData.get("title").toString() : "Not Found");
        url.setIp((String) rawData.get("ip"));

        return url;
    }

    public List<String> keyHandler(Set set) {
        List<String> list = new ArrayList<>();
        Iterator<String> iterator = set.iterator();

        while (iterator.hasNext()) {
            list.add(iterator.next());
        }

        return list;
    }
}
