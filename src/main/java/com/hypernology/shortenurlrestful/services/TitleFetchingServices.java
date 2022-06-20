package com.hypernology.shortenurlrestful.services;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class TitleFetchingServices {
    public String fetchUrlTitle(String rawUrl) {
        try {
            Document page = Jsoup.connect(rawUrl).get();
            return page.title();
        } catch (IOException e) {
            return "N/A";
        }
    }
}
