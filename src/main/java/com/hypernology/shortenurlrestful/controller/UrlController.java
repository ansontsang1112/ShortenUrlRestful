package com.hypernology.shortenurlrestful.controller;

import com.hypernology.shortenurlrestful.model.ShortenUrl;
import com.hypernology.shortenurlrestful.repository.ShortenUrlRepository;
import com.hypernology.shortenurlrestful.services.ResponseEntityService;
import com.hypernology.shortenurlrestful.services.SerializationServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class UrlController {
    private static final String PRODUCES = "application/json";

    @Autowired
    private SerializationServices serializationServices;

    @Autowired
    private ShortenUrlRepository shortenUrlRepository;

    @Autowired
    private ResponseEntityService responseEntityService;

    @RequestMapping(value = "/url/findAll", method = RequestMethod.GET, produces = PRODUCES)
    public List<ShortenUrl> urls() {
        return shortenUrlRepository.findAll();
    }

    @RequestMapping(value = "/url/{key}", method = RequestMethod.GET, produces = PRODUCES)
    public ShortenUrl urlById(@PathVariable String key) {
        return shortenUrlRepository.findByKey(key);
    }

    @RequestMapping(value = "/url/add", method = RequestMethod.POST, produces = PRODUCES)
    public ResponseEntity<ShortenUrl> urlSave(@RequestBody Map requestBody) {
        ShortenUrl shortenUrl = serializationServices.urlDeserializationServices(requestBody);

        if(shortenUrl.getKey() != null && shortenUrl.getUrl() != null) {
            String response = shortenUrlRepository.save(shortenUrl);

            Map responseMap = new HashMap();
            responseMap.put("code", 200);
            responseMap.put("key", response);
            responseMap.put("message", "URL added successfully");

            return responseEntityService.responseEntityGenerator("/url/add/%s", shortenUrl.getKey(), responseMap);
        } else {
            Map errorMap = new HashMap();
            errorMap.put("code", 500);
            errorMap.put("message", "Server Internal Error");

            return responseEntityService.responseEntityGenerator("/url/add/%s", "code", errorMap);
        }
    }
}
