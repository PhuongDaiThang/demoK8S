package com.demok8s.config;

import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.util.ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

@org.springframework.context.annotation.Configuration
public class K8sConfig {
    @Value("${k8s.master.url}")
    private String masterUrl;

    @Value("${k8s.secret.token}")
    private String token;

    @Bean
    public ApiClient k8sApiClient() {
        ApiClient client = new ClientBuilder()
                .setBasePath(masterUrl)
                .setVerifyingSsl(false)
                .build();

        client.addDefaultHeader("Authorization", "Bearer " + token);

        return client;
    }

    @Bean
    public CoreV1Api k8sCoreV1Api(ApiClient apiClient) {
        Configuration.setDefaultApiClient(apiClient);
        return new CoreV1Api();
    }

}
