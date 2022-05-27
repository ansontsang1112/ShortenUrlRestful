package com.hypernology.shortenurlrestful.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class ServerComponent implements WebServerFactoryCustomizer<ConfigurableWebServerFactory> {
    @Autowired
    private Environment environment;

    @Override
    public void customize(ConfigurableWebServerFactory factory) {
        factory.setPort(Integer.parseInt(environment.getProperty("srv.port")));
    }
}
