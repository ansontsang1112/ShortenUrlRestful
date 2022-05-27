package com.hypernology.shortenurlrestful.repository;


import com.hypernology.shortenurlrestful.model.ShortenUrl;

import java.util.List;

public interface ShortenUrlRepository  {
    List<ShortenUrl> findAll();
    <T> ShortenUrl findByKey(T key);

    ShortenUrl save(ShortenUrl shortenUrl);
    <K, V> ShortenUrl update(K key, List<V> valueHash);
}
