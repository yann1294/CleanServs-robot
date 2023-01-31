package com.webservice.springService.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/*
    This class uses the @ConfigurationProperties annotation to bind the properties in the application.
    properties file to the fields in the class,
    using the prefix attribute to specify the prefix of the properties.
* */

@Configuration
@ConfigurationProperties(prefix = "app")
public class AppProperties {

    private List<Server> servers;
    private int threshold;
    private int maxFolders; // instance variables

    public List<Server> getServers() {
        return servers;
    }

    public void setServers(List<Server> servers) {
        this.servers = servers;
    }

    public int getThreshold() {
        return threshold;
    }

    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }

    public int getMaxFolders() {
        return maxFolders;
    }

    public void setMaxFolders(int maxFolders) {
        this.maxFolders = maxFolders;
    }

    public static class Server {
        @Value("${Server.name}")
        private String name;

        @Value("${Server.directory}")
        private String directory;

        public Server(String name, String directory) {
            this.name = name;
            this.directory = directory;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDirectory() {
            return directory;
        }

        public void setDirectory(String directory) {
            this.directory = directory;
        }
    }
}

