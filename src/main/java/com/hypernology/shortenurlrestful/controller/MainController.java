package com.hypernology.shortenurlrestful.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class MainController {
    @RequestMapping(path = "/", method = RequestMethod.GET, produces = "application/json")
    public Map<String, String> index() {
        Map<String, String> map = new HashMap();
        map.put("users", "/user");
        map.put("urls", "/url");

        return map;
    }
}
