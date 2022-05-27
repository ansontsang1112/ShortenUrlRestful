package com.hypernology.shortenurlrestful.controller;

import com.hypernology.shortenurlrestful.model.ShortenUrl;
import com.hypernology.shortenurlrestful.repository.ShortenUrlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class UrlController {
    private static final String PRODUCES = "application/json";

    @Autowired
    private ShortenUrlRepository shortenUrlRepository;

    @RequestMapping(path = "/url/findAll", method = RequestMethod.GET, produces = PRODUCES)
    public List<ShortenUrl> urls() {
        return shortenUrlRepository.findAll();
    }

    @RequestMapping(path = "/url/{key}", method = RequestMethod.GET, produces = PRODUCES)
    public ShortenUrl urlById(@PathVariable String key) {
        return shortenUrlRepository.findByKey(key);
    }

    @RequestMapping(path = "/url/add", method = RequestMethod.POST, produces = PRODUCES)
    public ResponseEntity<ShortenUrl> urlSave(@RequestBody Map requestBody) {


        return null;
    }
}
