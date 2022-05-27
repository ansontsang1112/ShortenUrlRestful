package com.hypernology.shortenurlrestful.controller;

import com.hypernology.shortenurlrestful.model.ShortenUrl;
import com.hypernology.shortenurlrestful.repository.ShortenUrlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
}
