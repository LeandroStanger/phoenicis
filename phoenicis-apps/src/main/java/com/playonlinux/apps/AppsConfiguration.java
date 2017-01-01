package com.playonlinux.apps;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.playonlinux.multithreading.MultithreadingConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppsConfiguration {
    @Value("${application.repository.configuration}")
    private String repositoryConfiguration;

    @Autowired
    private MultithreadingConfiguration multithreadingConfiguration;

    @Bean
    public ApplicationsSource appsSource() {
        return new ConfigurableApplicationSource(repositoryConfiguration, new LocalApplicationsSource.Factory(new ObjectMapper()));
    }

    @Bean
    public ApplicationsSource backgroundAppsSource() {
        return new BackgroundApplicationsSource(appsSource(), multithreadingConfiguration.appsExecutorService());
    }

}
