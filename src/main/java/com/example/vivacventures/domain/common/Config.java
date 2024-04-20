package com.example.vivacventures.domain.common;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import java.io.InputStream;
import java.util.Properties;

@Getter
@Log4j2
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@org.springframework.context.annotation.Configuration
public class Config {
    private static Config instance = null;
    private final Properties p;

    public Config() {
        p = new Properties();
        try ( InputStream propertiesStream = this.getClass().getClassLoader().getResourceAsStream(DomainConstants.PROPERTIES_XML)) {
            p.loadFromXML(propertiesStream);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }


    public String getProperty(String key) {
        return p.getProperty(key);
    }


}
